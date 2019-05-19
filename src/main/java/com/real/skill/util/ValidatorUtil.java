package com.real.skill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: mabin
 * @create: 2019/5/16 9:59
 */
public class ValidatorUtil {

    private static final Pattern mobilePattern = Pattern.compile("1\\d{10}");

    private static final Pattern numPattern = Pattern.compile("^-?\\d+");

    /**
     * 使用正则表达式判断输入的手机号是否符合格式
     *
     * @param src
     * @return
     */
    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = mobilePattern.matcher(src);
        return matcher.matches();
    }

    public static boolean isNum(String src){
        if (StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = numPattern.matcher(src);
        return matcher.matches();
    }

}
