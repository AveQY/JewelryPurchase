/*
 Navicat Premium Dump SQL

 Source Server         : 我的服务器
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : jewelrypurchase

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 25/04/2025 15:40:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
CREATE TABLE IF NOT EXISTS `address`
(
    `address_id`   int                                                    NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `username`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
    `address_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货地址',
    `phone`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货人',
    PRIMARY KEY (`address_id` DESC) USING BTREE,
    INDEX `address_user_username` (`username` ASC) USING BTREE,
    CONSTRAINT `address_user_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2138
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for carouselimg
-- ----------------------------
CREATE TABLE IF NOT EXISTS `carouselimg`
(
    `id`        int                                                   NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
    `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '轮播图url',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for dashboard
-- ----------------------------
CREATE TABLE IF NOT EXISTS `dashboard`
(
    `dashboard_id` int                                                    NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `product_id`   int                                                    NOT NULL COMMENT '商品id',
    `goods_num`    int                                                    NULL DEFAULT NULL COMMENT '商品数量',
    `username`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '购物车所属用户名',
    PRIMARY KEY (`dashboard_id`) USING BTREE,
    INDEX `product_id` (`product_id` ASC) USING BTREE,
    INDEX `dashboard_user_username` (`username` ASC) USING BTREE,
    CONSTRAINT `dashboard_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `dashboard_user_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 48
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
CREATE TABLE IF NOT EXISTS `orders`
(
    `order_id`          bigint                                                                                               NOT NULL AUTO_INCREMENT COMMENT '订单号',
    `user_id`           int                                                                                                  NOT NULL COMMENT '购买用户ID',
    `product_id`        int                                                                                                  NOT NULL COMMENT '商品ID',
    `purchase_quantity` int                                                                                                  NULL DEFAULT NULL COMMENT '订单商品数量',
    `address_id`        int                                                                                                  NOT NULL COMMENT '收货人信息',
    `total_amount`      decimal(10, 2)                                                                                       NULL DEFAULT NULL COMMENT '购买金额',
    `status`            enum ('待支付','待发货','已发货','已完成','已取消') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待支付' COMMENT '订单状态',
    `payment_method`    enum ('微信支付','支付宝') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                          NULL DEFAULT '微信支付' COMMENT '付款方式',
    `create_time`       timestamp                                                                                            NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    `update_time`       timestamp                                                                                            NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
    PRIMARY KEY (`order_id`) USING BTREE,
    INDEX `idx_user_id` (`user_id` ASC) USING BTREE,
    INDEX `idx_create_time` (`create_time` ASC) USING BTREE,
    INDEX `orders_product_id` (`product_id` ASC) USING BTREE,
    INDEX `orders_address_id` (`address_id` ASC) USING BTREE,
    CONSTRAINT `orders_address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `orders_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2025042493594016001
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for product_click_stats
-- ----------------------------
CREATE TABLE IF NOT EXISTS `product_click_stats`
(
    `product_id`    int NOT NULL COMMENT '商品ID',
    `total_clicks`  int NOT NULL DEFAULT 0 COMMENT '总点击次数',
    `current_score` int NOT NULL DEFAULT 0 COMMENT '热度评分',
    PRIMARY KEY (`product_id`) USING BTREE,
    INDEX `idx_current_score` (`current_score` DESC) USING BTREE,
    CONSTRAINT `product_click_stats_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for products
-- ----------------------------
CREATE TABLE IF NOT EXISTS `products`
(
    `product_id`  int                                                           NOT NULL COMMENT '商品id',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '商品名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '商品描述',
    `price`       decimal(10, 2)                                                NULL     DEFAULT NULL COMMENT '价格',
    `category_id` int                                                           NOT NULL COMMENT '类别id',
    `stock`       int                                                           NULL     DEFAULT 0 COMMENT '库存数量',
    `image_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '商品图片链接',
    `username`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin        NOT NULL COMMENT '发布人用户名',
    `is_sell`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否上架（0，1）',
    `version`     int                                                           NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`product_id` DESC) USING BTREE,
    INDEX `category_id` (`category_id` ASC) USING BTREE,
    INDEX `products_user_username` (`username` ASC) USING BTREE,
    CONSTRAINT `products_category_id` FOREIGN KEY (`category_id`) REFERENCES `products_categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `products_user_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for products_categories
-- ----------------------------
CREATE TABLE IF NOT EXISTS `products_categories`
(
    `category_id`     int                                                           NOT NULL AUTO_INCREMENT COMMENT '类别ID',
    `categories_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名称',
    PRIMARY KEY (`category_id` DESC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 29
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for products_image
-- ----------------------------
CREATE TABLE IF NOT EXISTS `products_image`
(
    `image_id`   int                                                NOT NULL AUTO_INCREMENT COMMENT '图片id',
    `product_id` int                                                NOT NULL COMMENT '商品id',
    `image_url`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图片链接',
    PRIMARY KEY (`image_id`) USING BTREE,
    INDEX `product_id` (`product_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 195
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_similarities
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_similarities`
(
    `user_id`          int            NOT NULL COMMENT '用户ID',
    `similar_user_id`  int            NOT NULL COMMENT '相似用户ID',
    `similarity_score` decimal(10, 9) NOT NULL COMMENT '相似度',
    PRIMARY KEY (`user_id`, `similar_user_id`) USING BTREE,
    INDEX `similar_user_id` (`similar_user_id` ASC) USING BTREE,
    CONSTRAINT `user_similarities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `user_similarities_ibfk_2` FOREIGN KEY (`similar_user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE IF NOT EXISTS `users`
(
    `user_id`  int                                                           NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin        NOT NULL COMMENT '用户名',
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'MD5密码',
    `phone`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册手机号',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `username` (`username` ASC) USING BTREE,
    UNIQUE INDEX `username_2` (`username` ASC) USING BTREE,
    UNIQUE INDEX `phone` (`phone` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 53
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
