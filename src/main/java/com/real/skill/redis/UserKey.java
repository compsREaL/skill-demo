package com.real.skill.redis;


/**
 * @author: mabin
 * @create: 2019/5/15 19:19
 */
public class UserKey extends BasePrefix{


    private UserKey(String keyPrefix){
        super(keyPrefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

}
