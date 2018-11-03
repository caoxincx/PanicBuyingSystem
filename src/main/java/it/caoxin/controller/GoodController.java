package it.caoxin.controller;

import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.GoodKey;
import it.caoxin.result.Result;
import it.caoxin.service.GoodsService;
import it.caoxin.vo.GoodDetailVo;
import it.caoxin.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * 原来list,查询商品列表没有缓存到redis
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/list")
    public String goodList(Model model, User user){
        model.addAttribute("user",user);
        // 查询所有商品
        List<GoodsVo> allGoods = goodsService.getAllGoods();
        model.addAttribute("goodsList",allGoods);
        return "good";
    }

    /**
     * 将商品列表页面缓存到redis中
     * @param model
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String listGood(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        //1.添加user
        model.addAttribute("user",user);
        //2.取缓存中的html值
        String html = redisService.get(GoodKey.goodListToken, "", String.class);
        if (StringUtils.isNotEmpty(html)){
            return html;
        }
        //3.否则手动解析
        List<GoodsVo> goodList = goodsService.getAllGoods();
        model.addAttribute("goodsList",goodList);
        SpringWebContext webContext = new SpringWebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale()
                ,model.asMap(),
                applicationContext);
        String view = thymeleafViewResolver.getTemplateEngine().process("good", webContext);
        if (view != null){
            redisService.set(GoodKey.goodListToken,"",view);
        }
        return view;
    }

    /**
     * 商品详情页面没有缓存
     *
     * 页面静态化优化，加上GoodDetailVo
     * @param goodsId
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/detail/{id}")
    @ResponseBody
    public Result<GoodDetailVo> goodDetail(@PathVariable("id") long goodsId,
                                           Model model,
                                           User user){
        //查询对应的商品
        GoodsVo goods = goodsService.getGoodsById(goodsId);

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

        GoodDetailVo goodDetailVo = new GoodDetailVo();
        goodDetailVo.setGoods(goods);
        goodDetailVo.setUser(user);
        goodDetailVo.setPanicBuyingStatus(panicBuyingStatus);
        goodDetailVo.setRemainSeconds(remainSeconds);
        return Result.success(goodDetailVo);
    }

    /**
     * 商品详情页面增加缓存
     * @param goodsId
     * @param model
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/goodsdetail/{id}")
    @ResponseBody
    public String goodsDetail(@PathVariable("id") long goodsId,
                             Model model,
                             User user,
                              HttpServletRequest request,
                              HttpServletResponse response){

        String html = redisService.get(GoodKey.goodDetailToken, String.valueOf(goodsId), String.class);
        if (StringUtils.isNotEmpty(html)){
            return html;
        }
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

        SpringWebContext webContext = new SpringWebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale()
                ,model.asMap(),
                applicationContext);
        String goodDetailsView = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
        redisService.set(GoodKey.goodDetailToken,String.valueOf(goodsId),goodDetailsView);
        return goodDetailsView;
    }

}
