package com.real.skill.controller;

import com.real.skill.rabbitmq.RabbitMQSender;
import com.real.skill.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: mabin
 * @create: 2019/5/15 17:13
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @RequestMapping(value = "/thymeleaf",method = RequestMethod.GET)
    public String thymeleaf(){
        return "hello";
    }

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq(){
//        rabbitMQSender.send("rabbit  mq");
//        return Result.success("hello");
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> mqTopic(){
//        rabbitMQSender.sendTopic("rabbit topic  mq");
//        return Result.success("hello");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> mqFanout(){
//        rabbitMQSender.sendTopic("rabbit fanout  mq");
//        return Result.success("hello");
//    }
//
//    @RequestMapping("/mq/headers")
//    @ResponseBody
//    public Result<String> mqHeaders(){
//        rabbitMQSender.sendHeader("rabbit headers  mq");
//        return Result.success("hello");
//    }
}
