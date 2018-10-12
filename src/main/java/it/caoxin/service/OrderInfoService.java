package it.caoxin.service;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.mapper.OrderMapper;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderInfoService {
    @Autowired
    private OrderMapper orderMapper;

    /**
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public PbsOrderInfo getPbsOrderInfoByUserAndGoodsId(long userId,long goodsId){
        return orderMapper.getPbsOrderByUserAndGoodsId(userId,goodsId);
    }

    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods) {
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
        long orderId = orderMapper.insert(orderInfo);
        PbsOrderInfo pbsOrderInfo = new PbsOrderInfo();
        pbsOrderInfo.setGoodsId(goods.getId());
        pbsOrderInfo.setOrderId(orderId);
        pbsOrderInfo.setUserId(user.getId());
        orderMapper.insertMiaoshaOrder(pbsOrderInfo);
        return orderInfo;
    }
}
