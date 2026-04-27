package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "dashboard")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("购物车ID")
    @Column(name = "dashboard_id")
    private int dashboardId;

    @Comment("购物车商品ID")
    @Column(name = "product_id")
    private int productId;

    @Comment("购物车商品数量")
    @Column(name = "goods_num")
    private int goodsNum;

    @Comment("购物车所属用户名")
    @Column(name = "username")
    private String buyer;

    public Dashboard(int productId, int goodsNum, String buyer) {
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
