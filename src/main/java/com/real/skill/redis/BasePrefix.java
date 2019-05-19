package com.real.skill.redis;

/**
 * @author: mabin
 * @create: 2019/5/15 19:15
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String keyPrefix;

    public BasePrefix(int expireSeconds,String keyPrefix){
        this.expireSeconds = expireSeconds;
        this.keyPrefix = keyPrefix;
    }

    public BasePrefix(String keyPrefix){
        //0代表永不过期
        this(0,keyPrefix);
    }

    public int expireSeconds(){
        return expireSeconds;
    }

    public String getPrefix(){
        String className = getClass().getSimpleName();
        return className + ":" + keyPrefix;
    }

}
