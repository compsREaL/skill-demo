package com.real.skill.dao;

import com.real.skill.domain.SkillUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author: mabin
 * @create: 2019/5/16 10:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SkillUserDaoTest {

    @Autowired
    private SkillUserDao skillUserDao;

    @Test
    public void testSelectUserById(){
        SkillUser skillUser = skillUserDao.selectUserById(13412345678L);
        System.out.println(skillUser.getPassword());
    }

}