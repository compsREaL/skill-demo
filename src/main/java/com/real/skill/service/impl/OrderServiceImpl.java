package com.real.skill.service.impl;

import com.real.skill.dao.OrderDao;
import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillOrder;
import com.real.skill.domain.SkillUser;
import com.real.skill.redis.OrderKey;
import com.real.skill.redis.RedisService;
import com.real.skill.service.OrderService;
import com.real.skill.service.SkillService;
import com.real.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author: mabin
 * @create: 2019/5/16 16:23
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;

    @Override
    @Transactional
    public OrderInfo createOrder(SkillUser user, GoodsVo goods) {
        //插入订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(1l);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSkillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insertOrderInfo(orderInfo);
        //插入秒杀订单
        SkillOrder skillOrder = new SkillOrder();
        skillOrder.setGoodsId(goods.getId());
        skillOrder.setOrderId(orderInfo.getId());
        skillOrder.setUserId(user.getId());
        orderDao.insertSkillOrder(skillOrder);
        //把秒杀订单写入缓存
        redisService.set(OrderKey.getSkillOrderByUserIdAndGoodsId,""+user.getId()+"_"+goods.getId(),skillOrder);
        return orderInfo;
    }

    @Override
    public SkillOrder getSkillOrderByUserIdAndGoodsId(Long userId, long goodsId) {
//        return orderDao.selectOrderByUserIdAndGoodsId(userId,goodsId);
        return redisService.get(OrderKey.getSkillOrderByUserIdAndGoodsId,""+userId+"_"+goodsId,SkillOrder.class);
    }

    @Override
    public OrderInfo getOrderById(long orderId) {
        return orderDao.selectOrderById(orderId);
    }

    @Override
    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteSkillOrders();
    }
}
