package com.real.skill.interceptor;

import com.real.skill.domain.SkillUser;

/**
 * @author: mabin
 * @create: 2019/5/18 15:05
 */
public class UserContext {

    private static ThreadLocal<SkillUser> userThreadLocal=new ThreadLocal<SkillUser>();

    public static void setUser(SkillUser user){
        userThreadLocal.set(user);
    }

    public static SkillUser getUser(){
        return userThreadLocal.get();
    }
}
