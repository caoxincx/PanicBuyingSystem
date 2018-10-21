package it.caoxin.service;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.mapper.OrderMapper;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.OrderKey;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderInfoService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    /**
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public PbsOrderInfo getPbsOrderInfoByUserAndGoodsId(long userId,long goodsId){
        //1.加redis缓存优化
        PbsOrderInfo pbsOrderInfo = redisService.get(OrderKey.orderKey, "" + userId +"_"+ goodsId, PbsOrderInfo.class);
        return pbsOrderInfo;
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
        //订单信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        //抢购订单信息
        PbsOrderInfo pbsOrderInfo = new PbsOrderInfo();
        pbsOrderInfo.setGoodsId(goods.getId());
        pbsOrderInfo.setOrderId(orderInfo.getId());
        pbsOrderInfo.setUserId(user.getId());
        //将抢购信息放入Redis
        redisService.set(OrderKey.orderKey,""+pbsOrderInfo.getUserId()+"_"+pbsOrderInfo.getGoodsId(),pbsOrderInfo);
        orderMapper.insertMiaoshaOrder(pbsOrderInfo);
        return orderInfo;
    }

    public OrderInfo getOrderInfoById(long orderId){
        return orderMapper.getOrderById(orderId);
    }
}
