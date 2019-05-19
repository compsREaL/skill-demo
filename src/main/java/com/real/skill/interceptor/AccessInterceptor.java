package com.real.skill.interceptor;

import com.alibaba.fastjson.JSON;
import com.real.skill.annotation.AccessLimit;
import com.real.skill.domain.SkillUser;
import com.real.skill.redis.AccessKey;
import com.real.skill.redis.RedisService;
import com.real.skill.result.CodeMsg;
import com.real.skill.result.Result;
import com.real.skill.service.SkillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 *
 * @author: mabin
 * @create: 2019/5/18 14:50
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SkillUserService skillUserService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        if (handler instanceof HandlerMethod){
            SkillUser user = getUser(request,response);
            UserContext.setUser(user);

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit==null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin){
                if (user==null){
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_"+user.getId();
            }
            Integer count = redisService.get(AccessKey.accessKeyWithExpire(seconds),key,Integer.class);
            if (count==null){
                redisService.set(AccessKey.accessKeyWithExpire(seconds),key,1);
            } else if (count < maxCount){
                redisService.incr(AccessKey.accessKeyWithExpire(seconds),key);
            } else {
                render(response,CodeMsg.ACCESS_LIMIT);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response,CodeMsg codeMsg) {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            String string = JSON.toJSONString(Result.error(codeMsg));
            outputStream.write(string.getBytes("UTF-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private SkillUser getUser(HttpServletRequest request,HttpServletResponse response){

        //获取request中传递进来的token
        String paramToken = request.getParameter(SkillUserService.COOKIE_NAME_TOKEN);
        //获取Cookie中携带的token
        String cookieToken = getCookieValue(request,SkillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return skillUserService.getUserByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies==null || cookies.length==0){
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(tokenName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
