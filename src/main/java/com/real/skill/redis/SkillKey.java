package com.real.skill.redis;

/**
 * @author: mabin
 * @create: 2019/5/17 20:05
 */
public class SkillKey extends BasePrefix {


    public SkillKey(int expireSeconds,String keyPrefix) {
        super(expireSeconds,keyPrefix);
    }

    public static SkillKey isGoodsOver = new SkillKey(0,"go");

    public static SkillKey getSkillPath = new SkillKey(60,"sp");

    public static SkillKey getVerifyCode = new SkillKey(300,"vc");

}
