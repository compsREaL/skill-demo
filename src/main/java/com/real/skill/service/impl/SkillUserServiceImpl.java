package com.real.skill.service.impl;

import com.real.skill.dao.SkillUserDao;
import com.real.skill.domain.SkillUser;
import com.real.skill.exception.GlobalException;
import com.real.skill.redis.KeyPrefix;
import com.real.skill.redis.RedisService;
import com.real.skill.redis.SkillUserKey;
import com.real.skill.result.CodeMsg;
import com.real.skill.service.SkillUserService;
import com.real.skill.util.MD5Util;
import com.real.skill.util.UUIDUtil;
import com.real.skill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: mabin
 * @create: 2019/5/16 10:20
 */
@Service
public class SkillUserServiceImpl implements SkillUserService {

    @Autowired
    private SkillUserDao skillUserDao;
    @Autowired
    private RedisService redisService;

    /**
     * 通过用户id获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public SkillUser getUserById(long id) {
        //从缓存中获取数据
        SkillUser user = redisService.get(SkillUserKey.getById,""+id,SkillUser.class);
        if (user != null){
            return user;
        }
        //从数据库中获取数据
        user = skillUserDao.selectUserById(id);
        if (user!=null){
            redisService.set(SkillUserKey.getById,""+id,SkillUser.class);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean modifyPassword(long id,String password,String newPassword,String token){
        SkillUser user = getUserById(id);
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        if (!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        SkillUser newUser = new SkillUser();
        newUser.setId(id);
        newUser.setPassword(MD5Util.formPassToDBPass(newPassword,user.getSalt()));
        int effectRows = skillUserDao.updateUserPassword(newUser);
        if (effectRows<=0){
            throw new GlobalException(CodeMsg.PASSWORD_ALTER_ERROR);
        }
        //修改缓存。
        // 1.删除用户原先缓存信息
        redisService.delete(SkillUserKey.getById,""+id);
        //更新缓存
        //更新token的缓存
        user.setPassword(newUser.getPassword());
        redisService.set(SkillUserKey.token,token,user);
        return true;
    }

    /**
     * 登录用户的账号密码验证
     *
     * @param response
     * @param loginVo
     * @return
     */
    @Override
    public String login(HttpServletResponse response,LoginVo loginVo){
        if (loginVo==null){
            throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        SkillUser user = skillUserDao.selectUserById(Long.parseLong(mobile));
        if (user==null){
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        if (!MD5Util.formPassToDBPass(formPass,salt).equals(dbPass)){
            throw  new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response,user,token);
        return token;
    }

    /**
     * 通过浏览器端传入的cookie获取用户对象，并进行cookie的更新
     *
     * @param response
     * @param token
     * @return
     */
    @Override
    public SkillUser getUserByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        SkillUser user =  redisService.get(SkillUserKey.token,token,SkillUser.class);
        //延长cookie有效期
        if (user!=null) {
            addCookie(response, user,token);
        }
        return user;
    }

    /**
     * 为浏览器端写入cookie
     *
     * @param response
     * @param user
     */
    private void addCookie(HttpServletResponse response,SkillUser user,String token){
        //生成cookie
        redisService.set(SkillUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(SkillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
