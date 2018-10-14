package it.caoxin.vo;

import it.caoxin.domain.User;

public class GoodDetailVo {
    private User user;
    private GoodsVo goods;
    int panicBuyingStatus = 0;
    int remainSeconds = 0;

    public GoodDetailVo() {
    }

    public GoodDetailVo(User user, GoodsVo goods, int panicBuyingStatus, int remainSeconds) {
        this.user = user;
        this.goods = goods;
        this.panicBuyingStatus = panicBuyingStatus;
        this.remainSeconds = remainSeconds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public int getPanicBuyingStatus() {
        return panicBuyingStatus;
    }

    public void setPanicBuyingStatus(int panicBuyingStatus) {
        this.panicBuyingStatus = panicBuyingStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
