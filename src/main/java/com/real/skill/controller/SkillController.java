package com.real.skill.controller;

import com.real.skill.annotation.AccessLimit;
import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillOrder;
import com.real.skill.domain.SkillUser;
import com.real.skill.rabbitmq.RabbitMQSender;
import com.real.skill.rabbitmq.SkillMessage;
import com.real.skill.redis.*;
import com.real.skill.result.CodeMsg;
import com.real.skill.result.Result;
import com.real.skill.service.GoodsService;
import com.real.skill.service.OrderService;
import com.real.skill.service.SkillService;
import com.real.skill.util.MD5Util;
import com.real.skill.util.UUIDUtil;
import com.real.skill.util.ValidatorUtil;
import com.real.skill.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/16 16:12
 */
@Controller
@RequestMapping("/skill")
public class SkillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RabbitMQSender sender;


    /**
     * 生成验证码接口
     *
     * @param response
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/verifyCodeImg", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> createVerifyCodeImg(HttpServletResponse response, SkillUser user, @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //生成验证码
        BufferedImage image = skillService.createVerifyCodeImg(user,goodsId);
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SKILL_FAIL);
        }
        return null;
    }

    /**
     * 生成秒杀地址的接口
     *
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 5,maxCount = 5)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSkillPath(HttpServletRequest request,SkillUser user, @RequestParam("goodsId") long goodsId, @RequestParam("verifyCodeActual")String verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (verifyCode==null || verifyCode.length()==0 || !ValidatorUtil.isNum(verifyCode)){
            return Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
        int verifyCodeActual = Integer.valueOf(verifyCode);
        boolean checkVerifyCode = skillService.checkVerifyCode(verifyCodeActual,user,goodsId);
        if (!checkVerifyCode){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = skillService.createPath(user, goodsId);
        return Result.success(path);
    }

    /**
     * 秒杀成功，返回订单id
     * 秒杀失败，返回-1
     * 排队中，返回0
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> result(Model model, SkillUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        SkillOrder skillOrder = orderService.getSkillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (skillOrder != null) {
            return Result.success(skillOrder.getOrderId());
        } else {
            if (redisService.exists(SkillKey.isGoodsOver, "" + goodsId)) {
                return Result.success(-1L);
            } else {
                return Result.success(0L);
            }
        }
    }

    /**
     * 执行秒杀操作
     *
     * @param model
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    @RequestMapping(value = "/{path}/do_skill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSkill(Model model, SkillUser user, @RequestParam("goodsId") long goodsId, @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证秒杀地址path
        boolean check = skillService.check(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSkillGoodsStock, "" + goodsId);
        if (stock < 0) {
            return Result.error(CodeMsg.SKILL_OVER);
        }
        //判断是否已经参与过秒杀
        SkillOrder skillOrder = orderService.getSkillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (skillOrder != null) {
            return Result.error(CodeMsg.REPEATE_SKILL);
        }
        //入队
        SkillMessage skillMessage = new SkillMessage();
        skillMessage.setGoodsId(goodsId);
        skillMessage.setUser(user);
        sender.sendSkillMessage(skillMessage);
        return Result.success(0);   //排队中

//        //判断库存
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();
//        if (stock<=0){
//            return Result.error(CodeMsg.SKILL_OVER);
//        }
//        //判断是否秒杀成功
//        SkillOrder skillOrder = orderService.getSkillOrderByUserIdAndGoodsId(user.getId(),goodsId);
//        if (skillOrder!=null){
//            return Result.error(CodeMsg.REPEATE_SKILL);
//        }
//        //减库存，下订单，写入秒杀订单
//        OrderInfo orderInfo = skillService.skill(user,goods);
//        return Result.success(orderInfo);
    }

    /**
     * 系统初始化时，把秒杀的商品加载入redis缓存中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null || goodsList.size() == 0) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSkillGoodsStock, "" + goods.getId(), goods.getStockCount());
        }
    }

    private HashMap<Long, Boolean> localOverMap = new HashMap<>();

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getSkillGoodsStock, "" + goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getSkillOrderByUserIdAndGoodsId);
        redisService.delete(SkillKey.isGoodsOver);
        skillService.reset(goodsList);
        return Result.success(true);
    }
}
