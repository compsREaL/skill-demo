package com.real.skill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/17 17:23
 */
@Configuration
public class RabbitMQConfiguration {

    public static final String SKILL_QUEUE="skill.queue";

    @Bean
    public Queue queue(){
        return new Queue(SKILL_QUEUE,true);
    }




//    public static final String QUEUE = "queue";
//    public static final String TOPIC_QUEUE = "topicQueue";
//    public static final String TOPIC_EXCHANGE="topicExchange";
//    public static final String ROUTING_KEY = "topic.key";
//    public static final String FANOUT_EXCHANGE="fanoutExchange";
//    public static final String HEADERS_EXCHANGE="headersExchange";
//    public static final String HEADERS_QUEUE = "headers.queue";
//    /**
//     * direct模式交换机
//     *
//     * @return
//     */
//    @Bean
//    public Queue queue(){
//        return new Queue(QUEUE,true);
//    }
//

//    /**
//     * topic模式交换机
//     *
//     * @return
//     */
//    @Bean
//    public Queue topicQueue(){
//        return new Queue(TOPIC_QUEUE,true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//    @Bean
//    public Binding topicBinding(){
//        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(ROUTING_KEY);
//    }
//
//    /**
//     * Fanout模式交换机
//     *
//     * @return
//     */
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//    @Bean
//    public Binding fanoutBinding(){
//        return BindingBuilder.bind(topicQueue()).to(fanoutExchange());
//    }
//
//    /**
//     * Header模式交换机
//     *
//     * @return
//     */
//    @Bean
//    public HeadersExchange headersExchange(){
//        return new HeadersExchange(HEADERS_EXCHANGE);
//    }
//    @Bean
//    public Queue headersQueue1(){
//        return new Queue(HEADERS_QUEUE,true);
//    }
//    @Bean
//    public Binding headersBinding(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("header1","value1");
//        map.put("header2","value2");
//        return BindingBuilder.bind(headersQueue1()).to(headersExchange()).whereAll(map).match();
//    }
}
