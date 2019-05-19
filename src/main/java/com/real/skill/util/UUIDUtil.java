package com.real.skill.util;

import java.util.UUID;

/**
 * @author: mabin
 * @create: 2019/5/16 11:49
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
