package com.real.skill.redis;


/**
 * @author: mabin
 * @create: 2019/5/17 15:38
 */
public class OrderKey extends BasePrefix {

    private OrderKey(String keyPrefix) {
        super( keyPrefix);
    }

    public static OrderKey getSkillOrderByUserIdAndGoodsId = new OrderKey("soug");
}
