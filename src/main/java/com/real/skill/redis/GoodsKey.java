package com.real.skill.redis;

/**
 * @author: mabin
 * @create: 2019/5/16 22:35
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");

    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");

    public static GoodsKey getSkillGoodsStock = new GoodsKey(0,"gs");
}
