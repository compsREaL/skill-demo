package com.real.skill.rabbitmq;

import com.real.skill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: mabin
 * @create: 2019/5/17 17:22
 */
@Service
public class RabbitMQSender {

    private static Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSkillMessage(SkillMessage skillMessage) {
        String message = RedisService.convertFromBeanToString(skillMessage);
        logger.info("send message:{}",message);
        amqpTemplate.convertAndSend(RabbitMQConfiguration.SKILL_QUEUE,message);
    }





//    public void send(Object message){
//        String msg = RedisService.convertFromBeanToString(message);
//        logger.info("send message:"+msg);
//        amqpTemplate.convertAndSend(RabbitMQConfiguration.QUEUE,msg);
//    }
//
//    public void sendTopic(Object message){
//        String msg = RedisService.convertFromBeanToString(message);
//        logger.info("send topic message:"+msg);
//        amqpTemplate.convertAndSend(RabbitMQConfiguration.TOPIC_EXCHANGE,RabbitMQConfiguration.ROUTING_KEY,msg);
//    }
//
//    public void sendFanout(Object message){
//        String msg = RedisService.convertFromBeanToString(message);
//        logger.info("send FANOUT message:"+msg);
//        amqpTemplate.convertAndSend(RabbitMQConfiguration.FANOUT_EXCHANGE,"",msg);
//    }
//
//    public void sendHeader(Object message){
//        String msg = RedisService.convertFromBeanToString(message);
//        logger.info("send headers message:"+msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1","value1");
//        properties.setHeader("header2","value2");
//        Message obj = new Message(msg.getBytes(),properties);
//        amqpTemplate.convertAndSend(RabbitMQConfiguration.HEADERS_EXCHANGE,"",obj);
//    }


}
