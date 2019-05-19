package com.real.skill.redis;

/**
 * @author: mabin
 * @create: 2019/5/18 14:16
 */
public class AccessKey extends BasePrefix {
    private AccessKey(int expireSeconds, String keyPrefix) {
        super(expireSeconds, keyPrefix);
    }

    public static AccessKey accessKeyWithExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }
}
