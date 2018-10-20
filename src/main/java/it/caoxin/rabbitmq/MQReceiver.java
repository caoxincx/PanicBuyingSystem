package it.caoxin.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    @Autowired
    AmqpTemplate amqpTemplate ;

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MqConfig.QUEUE)
    public void receive(Message message){
        logger.info("receive--->"+message);

    }

    @RabbitListener(queues = MqConfig.TOPIC_QUEUE1)
    public void receiveTopic1(Message message){
        logger.info("receive--->"+message);
    }

    @RabbitListener(queues = MqConfig.TOPIC_QUEUE2)
    public void receiveTopic2(Message message){
        logger.info("receive--->"+message);
    }

    @RabbitListener(queues = MqConfig.HEADERS_QUEUE)
    public void receiveTopic3(byte[] bytes){
        logger.info("receive--->"+new String(bytes));
    }
}
