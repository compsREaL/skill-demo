package com.real.skill.rabbitmq;

import com.real.skill.domain.OrderInfo;
import com.real.skill.domain.SkillOrder;
import com.real.skill.domain.SkillUser;
import com.real.skill.redis.RedisService;
import com.real.skill.service.GoodsService;
import com.real.skill.service.OrderService;
import com.real.skill.service.SkillService;
import com.real.skill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: mabin
 * @create: 2019/5/17 17:22
 */
@Service
public class RabbitMQReceiver {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SkillService skillService;

    private static Logger logger = LoggerFactory.getLogger(RabbitMQReceiver.class);

    @RabbitListener(queues = RabbitMQConfiguration.SKILL_QUEUE)
    public void receive(String message){
        logger.info("receive message:{}",message);
        SkillMessage skillMessage = RedisService.convertFromStringToBean(message,SkillMessage.class);
        SkillUser user = skillMessage.getUser();
        long goodsId = skillMessage.getGoodsId();

        //检查库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock<=0){
            return;
        }
        //判断是否已经秒杀过商品
        SkillOrder skillOrder = orderService.getSkillOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if (skillOrder!=null){
            return;
        }
        //生成秒杀订单
         skillService.skill(user,goods);
    }




//    @RabbitListener(queues = RabbitMQConfiguration.QUEUE)
//    public void receiver(String message){
//        logger.info("receive message:"+message);
//    }
//
//    @RabbitListener(queues = RabbitMQConfiguration.TOPIC_QUEUE)
//    public void receiverTopic(String message){
//        logger.info("receive topic message:"+message);
//    }
//
//    @RabbitListener(queues = RabbitMQConfiguration.HEADERS_QUEUE)
//    public void receiverHeaders(byte[] message){
//        logger.info("receive headers message:"+new String(message));
//    }
}
