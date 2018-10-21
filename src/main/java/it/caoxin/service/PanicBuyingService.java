package it.caoxin.service;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.PbsKey;
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

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo panicBuying(User user, GoodsVo goodsVo){
        //减少库存
        boolean success = goodsService.reduceStockCount(goodsVo);
        if (success){
            //生成订单
            return orderInfoService.createOrder(user,goodsVo);
        }else {
            setPanicBuyingOver(goodsVo.getId());
            return null;
        }

    }

    /**
     * 获取抢购是否成功的结果
     * @param userId
     * @param goodsId
     * @return
     */
    public long getPbsResult(Long userId, long goodsId) {
        PbsOrderInfo pbsOrder = orderInfoService.getPbsOrderInfoByUserAndGoodsId(userId, goodsId);
        if (pbsOrder != null){
            return pbsOrder.getOrderId();
        }else {
            boolean isOver = getPanicBuyingOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }
    private void setPanicBuyingOver(Long goodsId) {
        redisService.set(PbsKey.notMoreGoods,String.valueOf(goodsId),true);
    }
    private boolean getPanicBuyingOver(long goodsId) {
        return redisService.exists(PbsKey.notMoreGoods,String.valueOf(goodsId));
    }
}
