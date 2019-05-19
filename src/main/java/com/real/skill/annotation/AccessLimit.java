package com.real.skill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 限流注解
 *
 * @author: mabin
 * @create: 2019/5/18 14:45
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface AccessLimit {

    int seconds();
    int maxCount();
    boolean needLogin() default true;
}
