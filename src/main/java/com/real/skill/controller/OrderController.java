package com.real.skill.controller;

import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillUser;
import com.real.skill.result.CodeMsg;
import com.real.skill.result.Result;
import com.real.skill.service.GoodsService;
import com.real.skill.service.OrderService;
import com.real.skill.vo.GoodsVo;
import com.real.skill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: mabin
 * @create: 2019/5/17 14:50
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public Result<OrderDetailVo> getOrderDetail(Model model, SkillUser user, @RequestParam("orderId")long orderId){
        if (user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (orderId<=0){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        //获取商品信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoods(goods);
        orderDetailVo.setOrderInfo(orderInfo);
        return Result.success(orderDetailVo);
    }
}
