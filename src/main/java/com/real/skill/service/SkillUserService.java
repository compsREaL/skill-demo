package com.real.skill.service;

import com.real.skill.domain.SkillUser;
import com.real.skill.result.CodeMsg;
import com.real.skill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: mabin
 * @create: 2019/5/16 10:18
 */
public interface SkillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    /**
     * 根据用户id查找用户
     *
     * @return
     */
    public SkillUser getUserById(long id);

    /**
     * 对登录的用户进行账号密码校验
     *
     * @param loginVo
     * @return
     */
    public String login(HttpServletResponse response,LoginVo loginVo);

    /**
     * 根据用户的token返回用户的信息
     *
     * @param response
     * @param token
     * @return
     */
    SkillUser getUserByToken(HttpServletResponse response,String token);

    /**
     * 更改用户密码
     *
     * @param id
     * @param password
     * @param newPassword
     * @param token
     * @return
     */
    public boolean modifyPassword(long id,String password,String newPassword,String token);
}
