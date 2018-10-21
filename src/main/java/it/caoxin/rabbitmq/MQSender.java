package it.caoxin.rabbitmq;

import it.caoxin.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void panicGoods(PbsMessage pbsMessage){
        String pbsMsg = RedisService.beanToString(pbsMessage);
        logger.info("pbsMsg:{}"+pbsMsg);
        amqpTemplate.convertAndSend(MqConfig.PBS_QUEUE,pbsMsg);
    }



    public void send(Object object){
        String message = RedisService.beanToString(object);
        logger.info("send:--->"+message);
        amqpTemplate.convertAndSend(MqConfig.QUEUE,message);
    }

    public void sendToTopic(Object object){
        String message = RedisService.beanToString(object);
        logger.info("send:--->"+message);
        amqpTemplate.convertAndSend(MqConfig.TOPIC_ENXCHANGE,"topic.key1",message);
        amqpTemplate.convertAndSend(MqConfig.TOPIC_ENXCHANGE,"topic.key2",message);
    }

    public void sendToFanout(Object object){
        String message = RedisService.beanToString(object);
        logger.info("send:--->"+message);
        amqpTemplate.convertAndSend(MqConfig.FANOUT_EXCHANGE,"",message);

    }

    public void sendHeader(Object object){
        String message = RedisService.beanToString(object);
        logger.info("send:--->"+message);

        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");

        Message msg = new Message(message.getBytes(), properties);
        amqpTemplate.convertAndSend(MqConfig.HEADERS_EXCHANGE,"",msg);
    }
}
