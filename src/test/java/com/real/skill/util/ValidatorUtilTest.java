package com.real.skill.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author: mabin
 * @create: 2019/5/16 10:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ValidatorUtilTest {

    @Test
    public void test(){
        String mobile1 = "13412345678";
        String mobile2 = "123114141";
        String mobile3 = "1231asd1311";

        boolean b1 = ValidatorUtil.isMobile(mobile1);
        boolean b2 = ValidatorUtil.isMobile(mobile2);
        boolean b3 = ValidatorUtil.isMobile(mobile3);
        assertEquals(true,b1);
        assertEquals(false,b2);
        assertEquals(false,b3);
    }

}