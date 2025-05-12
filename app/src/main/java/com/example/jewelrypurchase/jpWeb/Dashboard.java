package com.example.jewelrypurchase.jpWeb;

public class Dashboard {

    private int dashboardId;

    private int productId;

    private int goodsNum;

    private String buyer;

    public Dashboard(int dashboardId, int productId, int goodsNum, String buyer) {
        this.dashboardId = dashboardId;
        this.productId = productId;
        this.goodsNum = goodsNum;
        this.buyer = buyer;
    }

    public Dashboard() {
    }

    public int getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(int dashboardId) {
        this.dashboardId = dashboardId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
}
