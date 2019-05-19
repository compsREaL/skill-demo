package com.real.skill.controller;

import com.real.skill.domain.SkillUser;
import com.real.skill.redis.GoodsKey;
import com.real.skill.redis.RedisService;
import com.real.skill.result.Result;
import com.real.skill.service.GoodsService;
import com.real.skill.vo.GoodsDetailVo;
import com.real.skill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/16 12:02
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list",method = RequestMethod.GET,produces = "text/html")
    @ResponseBody
    public String toGoodsList(HttpServletRequest request, HttpServletResponse response,Model model, SkillUser user){
        //从缓存中获取
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
//        return "goods_list";
        //手动渲染
        WebContext springWebContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",springWebContext);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}",method = RequestMethod.GET)
    @ResponseBody
    public Result<GoodsDetailVo> toGoodsDetail(HttpServletRequest request, HttpServletResponse response, Model model, SkillUser user, @PathVariable("goodsId")long goodsId){

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goods!=null) {
            model.addAttribute("goods", goods);
        }
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int skillStatus;
        int remainSeconds;

        if (now<startTime){     //秒杀未开始
            skillStatus = 0;
            remainSeconds = (int) ((startTime-now)/1000);
        } else if (now > endTime){
            skillStatus = 2;    //秒杀已结束
            remainSeconds = -1;
        } else {
            skillStatus = 1;    //秒杀进行中
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setSkillStatus(skillStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodsDetailVo);
    }

    @RequestMapping(value = "/to_detail2/{goodsId}",method = RequestMethod.GET,produces = "text/html")
    @ResponseBody
    public String toDetail(HttpServletRequest request,HttpServletResponse response,Model model,SkillUser user,@PathVariable("goodsId")long goodsId){
        //snowflake算法
        model.addAttribute("user",user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goods!=null) {
            model.addAttribute("goods", goods);
        }

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int skillStatus;
        int remainSeconds;

        if (now<startTime){     //秒杀未开始
            skillStatus = 0;
            remainSeconds = (int) ((startTime-now)/1000);
        } else if (now > endTime){
            skillStatus = 2;    //秒杀已结束
            remainSeconds = -1;
        } else {
            skillStatus = 1;    //秒杀进行中
            remainSeconds = 0;
        }
        model.addAttribute("skillStatus",skillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
//        return "goods_detail";
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染html页面
        WebContext springWebContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",springWebContext);
        if (html!=null){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }
}
