package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "purchase_quantity")
    private int purchaseQuantity;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "address_id")
    private int addressId;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", columnDefinition = "VARCHAR(20) CHECK (status IN ('待支付','待发货','已发货','已完成','已取消'))")
    private String status;

    @Column(name = "payment_method", columnDefinition = "VARCHAR(20) CHECK (payment_method IN ('微信支付','支付宝'))")
    private String paymentMethod;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public Orders() {

    }

    public Orders(Long orderId, int productId, int purchaseQuantity, int userId, int addressId, BigDecimal totalAmount, String status, String paymentMethod, LocalDateTime createTime, LocalDateTime updateTime) {
        this.orderId = orderId;
        this.productId = productId;
        this.purchaseQuantity = purchaseQuantity;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(int purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
