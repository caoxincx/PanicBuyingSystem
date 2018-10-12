package it.caoxin.domain;

import java.util.Date;

public class PbsGoods {
    private Long id;
    private Long goodsId;
    private Double pbPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public PbsGoods() {
    }

    public PbsGoods(Long id, Long goodsId, Double pbPrice, Integer stockCount, Date startDate, Date endDate) {
        this.id = id;
        this.goodsId = goodsId;
        this.pbPrice = pbPrice;
        this.stockCount = stockCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Double getPbPrice() {
        return pbPrice;
    }

    public void setPbPrice(Double pbPrice) {
        this.pbPrice = pbPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "PbsGoods{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", pbPrice=" + pbPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
