package com.real.skill.vo;

import com.real.skill.domain.OrderInfo;

/**
 * @author: mabin
 * @create: 2019/5/17 14:52
 */
public class OrderDetailVo {

    private GoodsVo goods;

    private OrderInfo orderInfo;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
