package it.caoxin.vo;

import it.caoxin.domain.OrderInfo;
import it.caoxin.domain.User;

public class OrderInfoVo {
    private OrderInfo orderInfo;
    private GoodsVo goodsVo;

    public OrderInfoVo() {
    }

    public OrderInfoVo(OrderInfo orderInfo, GoodsVo goodsVo) {
        this.orderInfo = orderInfo;
        this.goodsVo = goodsVo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }
}
