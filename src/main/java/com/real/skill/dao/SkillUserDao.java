package com.real.skill.dao;

import com.real.skill.domain.SkillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author: mabin
 * @create: 2019/5/16 10:15
 */
@Mapper
public interface SkillUserDao {

    @Select("select id,nickname,password,salt,head,register_date,last_login_date,login_count from skill_user where id=#{id}")
    public SkillUser selectUserById(@Param("id")long id);

    @Update("update skill_user set password=#{password} where id=#{id}")
    int updateUserPassword(SkillUser newUser);
}
