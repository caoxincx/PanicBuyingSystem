package it.caoxin.rabbitmq;

import it.caoxin.domain.User;

public class PbsMessage {
    private User user;
    private long goodsId;

    public PbsMessage() {
    }

    public PbsMessage(User user, long goodsId) {
        this.user = user;
        this.goodsId = goodsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
