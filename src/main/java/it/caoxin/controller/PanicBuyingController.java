package it.caoxin.controller;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import it.caoxin.service.GoodsService;
import it.caoxin.service.OrderInfoService;
import it.caoxin.service.PanicBuyingService;
import it.caoxin.service.UserService;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;

@Controller
@RequestMapping("panicbuying")
public class PanicBuyingController {
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

    @RequestMapping("/panicbuying")
    @ResponseBody
    public Result<OrderInfo> panicBuying(User user, Model model, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //1.判断库存,加锁解决,
        //2.在数据库抢购订单表中加唯一索引
        GoodsVo goods;

         goods = goodsService.getGoodsById(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否抢购成功,不能重复抢购
        PbsOrderInfo pbsOrderInfo = orderInfoService.getPbsOrderInfoByUserAndGoodsId(user.getId(), goodsId);
        if (pbsOrderInfo != null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //减少库存，写入表数据
        OrderInfo orderInfo = panicBuyingService.panicBuying(user, goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);

        return Result.success(orderInfo);
    }
}
