package com.real.skill.service.impl;

import com.real.skill.domain.Goods;
import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillUser;
import com.real.skill.redis.RedisService;
import com.real.skill.redis.SkillKey;
import com.real.skill.service.GoodsService;
import com.real.skill.service.OrderService;
import com.real.skill.service.SkillService;
import com.real.skill.util.MD5Util;
import com.real.skill.util.UUIDUtil;
import com.real.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * @author: mabin
 * @create: 2019/5/16 16:28
 */
@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;


    @Override
    @Transactional
    public OrderInfo skill(SkillUser user, GoodsVo goods) {
        //减少库存
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            //下订单
            OrderInfo orderInfo = orderService.createOrder(user, goods);
            return orderInfo;
        }else {
            redisService.set(SkillKey.isGoodsOver,""+goods.getId(),true);
            return null;
        }
    }

    @Override
    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }

    @Override
    public boolean check(SkillUser user, long goodsId, String path) {
        if (user==null || path==null){
            return false;
        }
        String redisPath = redisService.get(SkillKey.getSkillPath,""+user.getId()+"_"+goodsId,String.class);
        if (redisPath!=null && redisPath.equals(path)){
            return true;
        }
        return false;
    }

    @Override
    public String createPath(SkillUser user, long goodsId) {
        if(user == null || goodsId<=0){
            return null;
        }
        String string = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(SkillKey.getSkillPath,""+user.getId()+"_"+goodsId,string);
        return string;
    }

    @Override
    public BufferedImage createVerifyCodeImg(SkillUser user, long goodsId) {
        if(user == null || goodsId<=0){
            return null;
        }

        //生成验证码图片
        int width = 80;
        int height = 32;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        //设置背景颜色
        graphics.setColor(new Color(0xDCDCDC));
        graphics.fillRect(0,0,width,height);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0,0,width-1,height-1);
        //随机数字生成器
        Random random = new Random();
        //生成干扰点
        for (int i=0;i<50;i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            graphics.drawOval(x,y,0,0);
        }
        //生成随机数字表达式
        String expression = createVerifyCode();
        graphics.setColor(new Color(1,100,0));
        graphics.setFont(new Font("Candara",Font.BOLD,24));
        graphics.drawString(expression,8,24);
        graphics.dispose();
        //把验证码存到redis中
        int rnd = calc(expression);
        redisService.set(SkillKey.getVerifyCode,""+user.getId()+"_"+goodsId,rnd);
        //输出图片
        return image;
    }

    @Override
    public boolean checkVerifyCode(int verifyCodeActual, SkillUser user, long goodsId) {
        if (user == null || goodsId<=0){
            return false;
        }
        Integer verifyCodeInRedis = redisService.get(SkillKey.getVerifyCode,""+user.getId()+"_"+goodsId,Integer.class);
        if (verifyCodeInRedis==null || verifyCodeActual-verifyCodeInRedis!=0){
            return false;
        }
        //验证码判断成功后从redis中移除
        redisService.delete(SkillKey.getVerifyCode,""+user.getId()+"_"+goodsId);
        return true;
    }


    private int calc(String expression) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (int) engine.eval(expression);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] operators = {'+','-','*'};

    private String createVerifyCode() {
        Random random = new Random();
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);

        char operator1 = operators[random.nextInt(3)];
        char operator2 = operators[random.nextInt(3)];
        String expression = ""+ num1 + operator1 + num2 + operator2 +num3;
        return expression;
    }
}
