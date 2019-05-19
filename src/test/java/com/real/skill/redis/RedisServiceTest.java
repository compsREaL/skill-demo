package com.real.skill.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: mabin
 * @create: 2019/5/15 18:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testSet(){
        boolean boo = redisService.set(UserKey.getById,"key1","1234");
        assertEquals(true,boo);
    }

    @Test
    public void testGet(){
        String value = redisService.get(UserKey.getById,"key1",String.class);
        assertEquals("1234",value);
        System.out.println(value);
    }
}