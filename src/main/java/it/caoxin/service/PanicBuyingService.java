package it.caoxin.service;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.PbsOrderInfo;
import it.caoxin.domain.User;
import it.caoxin.redis.RedisService;
import it.caoxin.redis.key.PbsKey;
import it.caoxin.utils.MD5Util;
import it.caoxin.utils.UUIDUtil;
import it.caoxin.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

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

    public boolean checkVerifyCode(User user, Long goodsId, int verifyCode) {
        if (user == null){
            return false;
        }

        Integer oldVerifyCode =
                redisService.get(PbsKey.getPbsVerifyCode,String.valueOf(user.getId())+","+String.valueOf(goodsId),Integer.class);
        if (oldVerifyCode == null || !oldVerifyCode.equals(verifyCode)){
            return false;
        }

        // 验证成功后删除验证码
        redisService.delete(PbsKey.getPbsVerifyCode,String.valueOf(user.getId())+","+String.valueOf(goodsId));
        return true;
    }

    /**
     * 通过缓存创建动态路径
     * @param user
     * @param goodsId
     * @return
     */
    public String createPanicBuyingPath(User user, Long goodsId) {
        if (user == null || goodsId <= 0){
            return null;
        }
        String path = MD5Util.md5(UUIDUtil.uuid() + "123456");
        // 将请求路径放入缓存
        redisService.set(PbsKey.getPbsPath,String.valueOf(user.getId())+"_"+String.valueOf(goodsId),path);
        return path;

    }

    /**
     * 验证动态生成的路径
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || goodsId <= 0){
            return false;
        }
        String oldPath = redisService.get(PbsKey.getPbsPath, String.valueOf(user.getId()) + "_" + String.valueOf(goodsId), String.class);
        return path.equals(oldPath);
    }

    /**
     * 生成验证图片
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(User user, Long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(PbsKey.getPbsVerifyCode, String.valueOf(user.getId()+","+goodsId), rnd);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
