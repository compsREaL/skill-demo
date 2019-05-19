package com.real.skill.redis;

/**
 * @author: mabin
 * @create: 2019/5/15 19:12
 */
public interface KeyPrefix {

    /**
     * Redis缓存的过期时间
     * @return
     */
    public int expireSeconds();

    /**
     * Redis缓存的通用前缀
     * @return
     */
    public String getPrefix();
}
