package it.caoxin.controller;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.result.CodeMsg;
import it.caoxin.result.Result;
import it.caoxin.service.GoodsService;
import it.caoxin.service.OrderInfoService;
import it.caoxin.service.UserService;
import it.caoxin.vo.GoodsVo;
import it.caoxin.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderInfoVo> orderDetail(User user, @RequestParam("orderId") long orderId){
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo order = orderInfoService.getOrderInfoById(orderId);

        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsById(goodsId);

        OrderInfoVo orderInfoVo = new OrderInfoVo(order, goods);
        return Result.success(orderInfoVo);
    }
}
