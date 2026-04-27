package com.aweqy.jewelrypurchaseweb.jpw;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum StatusEnum {
    PENDING_PAYMENT(1, "待支付"),
    PAID(2, "待发货"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消");

    private final int statusCode;
    private final String description;

    // 构造函数（默认private）
    StatusEnum(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    // 根据状态码获取枚举实例
    public static StatusEnum fromCode(int code) {
        return Arrays.stream(StatusEnum.values())
                .filter(status -> status.statusCode == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("无效状态码: " + code + "，有效范围：1-5"));
    }

    // 获取状态码（可用于数据库存储）
    @JsonValue  // 序列化时直接返回状态码
    public int getStatusCode() {
        return statusCode;
    }

    // 获取描述（用于前端展示）
    public String getDescription() {
        return description;
    }

    // 示例：判断是否允许取消订单
    public boolean canBeCancelled() {
        return this == PENDING_PAYMENT || this == PAID;
    }

    @Override
    public String toString() {
        return description;
    }
}
