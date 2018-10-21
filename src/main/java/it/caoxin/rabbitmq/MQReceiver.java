package it.caoxin.rabbitmq;

import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.service.GoodsService;
import it.caoxin.service.OrderInfoService;
import it.caoxin.service.PanicBuyingService;
import it.caoxin.service.UserService;
import it.caoxin.vo.GoodsVo;
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
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    PanicBuyingService panicBuyingService;

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * 从mq接受信息并下单过程
     * @param pbsMsg
     */
    @RabbitListener(queues = MqConfig.PBS_QUEUE)
    public void panicBuying(String pbsMsg){
        logger.info("pbsMsg",pbsMsg);
        PbsMessage pbsMessage = RedisService.stringToBean(pbsMsg, PbsMessage.class);

        long goodsId = pbsMessage.getGoodsId();
        User user = pbsMessage.getUser();

        //1.判断商品数量是否少于0
        GoodsVo goods = goodsService.getGoodsById(goodsId);
        if (goods.getStockCount() <= 0){
            return;
        }

        //2.判断是否已经抢购过
        PbsOrderInfo pbsOrder = orderInfoService.getPbsOrderInfoByUserAndGoodsId(user.getId(), goodsId);
        if (pbsOrder != null){
            return;
        }

        //3.下单
        panicBuyingService.panicBuying(user,goods);

    }
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
