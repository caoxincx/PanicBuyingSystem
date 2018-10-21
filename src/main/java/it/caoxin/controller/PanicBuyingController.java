package it.caoxin.controller;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.rabbitmq.MQReceiver;
import it.caoxin.rabbitmq.MQSender;
import it.caoxin.rabbitmq.PbsMessage;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.GoodKey;
import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import it.caoxin.service.GoodsService;
import it.caoxin.service.OrderInfoService;
import it.caoxin.service.PanicBuyingService;
import it.caoxin.service.UserService;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("panicbuying")
public class PanicBuyingController implements InitializingBean{
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

    @Autowired
    MQSender mqSender;

    private HashMap<Long,Boolean> isGoodOverMap = new HashMap<>();
    /**
     * 在系统初始化的时改变将对应的商品及数量加载到Redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> allGoods = goodsService.getAllGoods();
        if (allGoods == null || allGoods.size() <= 0){
           return;
        }
        for (GoodsVo goodsVo : allGoods){
            redisService.set(GoodKey.goodCount,""+goodsVo.getId(),goodsVo.getStockCount());
            isGoodOverMap.put(goodsVo.getId(),false);
        }
    }

    @RequestMapping("/panicbuying")
    @ResponseBody
    public Result<Integer> panicBuying(User user, Model model, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //先从内容标记判断，商品是否已经抢购完成
        if (isGoodOverMap.get(goodsId)){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //1.优化后的实现，先从redis中预减库存
        Long count = redisService.decr(GoodKey.goodCount, "" + goodsId);

        if (count < 0){
            isGoodOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //2.判断是否已经抢购过商品
        PbsOrderInfo pbsOrder= orderInfoService.getPbsOrderInfoByUserAndGoodsId(user.getId(), goodsId);
        if (pbsOrder != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //3.异步下单
        PbsMessage pbsMessage = new PbsMessage(user, goodsId);
        mqSender.panicGoods(pbsMessage);

        //4.返回前端排队中
       return Result.success(0);



//        没有优化的实现
//        //1.判断库存,加锁解决,
//        //2.在数据库抢购订单表中加唯一索引
//        GoodsVo goods;
//
//        goods = goodsService.getGoodsById(goodsId);
//        Integer stockCount = goods.getStockCount();
//        if (stockCount <= 0){
//            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
//        //判断是否抢购成功,不能重复抢购
//        PbsOrderInfo pbsOrderInfo = orderInfoService.getPbsOrderInfoByUserAndGoodsId(user.getId(), goodsId);
//        if (pbsOrderInfo != null){
//            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//        }
//
//        //减少库存，写入表数据
//        OrderInfo orderInfo = panicBuyingService.panicBuying(user, goods);
//        model.addAttribute("orderInfo",orderInfo);
//        model.addAttribute("goods",goods);
//
//        return Result.success(orderInfo);
    }

    @RequestMapping("/result")
    @ResponseBody
    public Result<Long> panicBuyingResult(User user, Model model, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = panicBuyingService.getPbsResult(user.getId(),goodsId);
        return Result.success(result);
    }

}
