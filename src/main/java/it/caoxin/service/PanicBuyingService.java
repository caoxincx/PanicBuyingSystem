package it.caoxin.service;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.User;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PanicBuyingService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Transactional
    public OrderInfo panicBuying(User user, GoodsVo goodsVo){
        //减少库存
        goodsService.reduceStockCount(goodsVo);
        //生成订单
        return orderInfoService.createOrder(user,goodsVo);
    }

}
