package com.aweqy.jewelrypurchaseweb.jpw;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PaymentMethodEnum {
    WECHAT(1,"微信支付"),
    PAY(2,"支付宝");

    private final int code;
    private final String description;

    // 构造函数（默认private）
    PaymentMethodEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // 根据状态码获取枚举实例
    public static PaymentMethodEnum fromCode(int code) {
        return Arrays.stream(PaymentMethodEnum.values())
                .filter(paymentMethod -> paymentMethod.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("无效状态码: " + code + "，有效范围：1-2"));
    }

    // 获取状态码（可用于数据库存储）
    @JsonValue  // 序列化时直接返回状态码
    public int getStatusCode() {
        return code;
    }

    // 获取描述（用于前端展示）
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
