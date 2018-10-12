package it.caoxin.vo;

import it.caoxin.domain.Goods;

import java.util.Date;

public class GoodsVo extends Goods{
    private Double pbPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public GoodsVo() {
    }

    public GoodsVo(Double pbPrice, Integer stockCount, Date startDate, Date endDate) {
        this.pbPrice = pbPrice;
        this.stockCount = stockCount;
        this.startDate = startDate;
        this.endDate = endDate;
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
        return "GoodsVo{" +
                "pbPrice=" + pbPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
