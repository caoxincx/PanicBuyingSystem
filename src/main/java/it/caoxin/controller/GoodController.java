package it.caoxin.controller;

import it.caoxin.domain.User;
import it.caoxin.service.GoodsService;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodController {
    @Autowired
    private GoodsService goodsService;


    @RequestMapping("/list")
    public String goodList(Model model, User user){
        model.addAttribute("user",user);
        // 查询所有商品
        List<GoodsVo> allGoods = goodsService.getAllGoods();
        model.addAttribute("goodsList",allGoods);
        return "good";
    }

    @RequestMapping("/detail/{id}")
    public String goodDetail(@PathVariable("id") long goodsId,
                             Model model,
                             User user){
        model.addAttribute("user",user);
        //查询对应的商品
        GoodsVo goods = goodsService.getGoodsById(goodsId);
        model.addAttribute("goods",goods);

        //商品抢购判断
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int panicBuyingStatus = 0;
        int remainSeconds = 0;

        if (now < startTime){
            panicBuyingStatus = 0;
            remainSeconds = (int)((startTime-now)/1000);
        }else if (now > endTime){
            panicBuyingStatus = 2;
            remainSeconds = -1;
        }else {
            panicBuyingStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("panicBuyingStatus",panicBuyingStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";

    }
}
