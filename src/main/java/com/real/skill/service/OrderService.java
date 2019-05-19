package com.real.skill.service;

import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillOrder;
import com.real.skill.domain.SkillUser;
import com.real.skill.vo.GoodsVo;

/**
 * @author: mabin
 * @create: 2019/5/16 16:22
 */
public interface OrderService {

    /**
     * 生成秒杀订单
     *
     * @param user
     * @param goods
     * @return
     */
    public OrderInfo createOrder(SkillUser user, GoodsVo goods);

    /**
     * 根据用户id和商品id查询订单
     *
     * @param userId
     * @param goodsId
     * @return
     */
    SkillOrder getSkillOrderByUserIdAndGoodsId(Long userId, long goodsId);

    /**
     * 根据订单号查询订单详情
     *
     * @param orderId
     * @return
     */
    OrderInfo getOrderById(long orderId);

    void deleteOrders();
}
