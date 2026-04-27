package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("唯一ID")
    @Column(name = "address_id")
    private int addressId;

    @Comment("用户名")
    @Column(name = "username", nullable = false)
    private String userId;

    @Comment("收货地址")
    @Column(name = "address_name")
    private String addressName;

    @Comment("手机号")
    @Column(name = "phone")
    private String phone;

    @Comment("收货人")
    @Column(name = "name")
    private String username;

    public Address() {

    }

    public Address(String userId, String addressName, String phone, String username) {
        this.userId = userId;
        this.addressName = addressName;
        this.phone = phone;
        this.username = username;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
