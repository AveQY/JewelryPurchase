package com.example.jewelrypurchase.jpWeb;

/**
 * 服务器部署
 */
public class WebUrl {

    // 安卓模拟器（本地调试）
    private final String BASE_URL = "http://10.0.2.2:6601";
    // 电脑连接手机
    private final String BASE_URL2 = "http://yourIP:6601";
    // 服务器（已部署）
    private final String BASE_URL3 = "http://aweqy.asia/JewelryPurchase";

    public String getBASE_URL() {
        // 本地调试将 返回值 改为 BASE_URL，记得提前部署好后端服务
        return BASE_URL3;
    }

}
