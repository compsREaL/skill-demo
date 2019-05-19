package com.real.skill.redis;

/**
 * @author: mabin
 * @create: 2019/5/16 11:52
 */
public class SkillUserKey extends BasePrefix{

    private static final int ERPIRE_SECONDS = 24*3600*2;

    public SkillUserKey(int expireSeconds,String keyPrefix) {
        super(expireSeconds,keyPrefix);
    }

    public static SkillUserKey token = new SkillUserKey(ERPIRE_SECONDS,"tk");
    public static SkillUserKey getById = new SkillUserKey(0,"id");
}
