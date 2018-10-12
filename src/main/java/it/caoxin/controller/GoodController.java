package it.caoxin.controller;

import it.caoxin.domain.User;
import it.caoxin.service.GoodsService;
import it.caoxin.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodController {
    @Autowired
    private GoodsService goodsService;
    @RequestMapping("/list")
    public String good(Model model, User user){
        model.addAttribute("user",user);
        // 查询所有商品
        List<GoodsVo> allGoods = goodsService.getAllGoods();
        model.addAttribute("goodsList",allGoods);
        return "good";
    }
}
