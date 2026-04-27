package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;

@Entity
@Table(name = "product_click_stats")
public class    ProductClickStats {

    @Id
    @Column(name = "product_id")
    private int productId;

    @Column(name = "total_clicks")
    private int totalClicks;

    @Column(name = "current_score")
    private int currentScore;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(int totalClicks) {
        this.totalClicks = totalClicks;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }
}
