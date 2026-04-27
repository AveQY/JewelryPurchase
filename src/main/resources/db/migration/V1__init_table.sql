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

 Date: 25/04/2025 16:23:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
                            `address_id` int NOT NULL AUTO_INCREMENT COMMENT '地址ID',
                            `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
                            `address_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货地址',
                            `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货人',
                            PRIMARY KEY (`address_id` DESC) USING BTREE,
                            INDEX `address_user_username`(`username` ASC) USING BTREE,
                            CONSTRAINT `address_user_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for carouselimg
-- ----------------------------
DROP TABLE IF EXISTS `carouselimg`;
CREATE TABLE `carouselimg`  (
                                `id` int NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
                                `image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '轮播图url',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of carouselimg
-- ----------------------------
INSERT INTO `carouselimg` VALUES (1, 'http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E5%9B%BE/1.png?sign=w05Epeuh8O4i9jreh8PQQi9GaGwje0mk6DFPo2TsNhY=:0');
INSERT INTO `carouselimg` VALUES (2, 'http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E5%9B%BE/b3351d756c7943508f3418e84d20b971_2.png?sign=Dxx5CWtzGZ_VXOM2Da58LdThuh1yKvDaSGAjLSEDED0=:0');
INSERT INTO `carouselimg` VALUES (3, 'http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E5%9B%BE/3.png?sign=JkaAqodlACxqEymR_sX8MfiU62orFFkn2XQLscihVrc=:0');
INSERT INTO `carouselimg` VALUES (4, 'http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E5%9B%BE/b3351d756c7943508f3418e84d20b971_3.png?sign=-P1RevzqEXoV2875ZshnBlbNrIDE9YPxzxUEUQ-7rYY=:0');

-- ----------------------------
-- Table structure for dashboard
-- ----------------------------
DROP TABLE IF EXISTS `dashboard`;
CREATE TABLE `dashboard`  (
                              `dashboard_id` int NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
                              `product_id` int NOT NULL COMMENT '商品id',
                              `goods_num` int NULL DEFAULT NULL COMMENT '商品数量',
                              `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '购物车所属用户名',
                              PRIMARY KEY (`dashboard_id`) USING BTREE,
                              INDEX `product_id`(`product_id` ASC) USING BTREE,
                              INDEX `dashboard_user_username`(`username` ASC) USING BTREE,
                              CONSTRAINT `dashboard_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT `dashboard_user_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dashboard
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
                           `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单号',
                           `user_id` int NOT NULL COMMENT '购买用户ID',
                           `product_id` int NOT NULL COMMENT '商品ID',
                           `purchase_quantity` int NULL DEFAULT NULL COMMENT '订单商品数量',
                           `address_id` int NOT NULL COMMENT '收货人信息',
                           `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '购买金额',
                           `status` enum('待支付','待发货','已发货','已完成','已取消') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待支付' COMMENT '订单状态',
                           `payment_method` enum('微信支付','支付宝') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '微信支付' COMMENT '付款方式',
                           `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                           `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
                           PRIMARY KEY (`order_id`) USING BTREE,
                           INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
                           INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
                           INDEX `orders_product_id`(`product_id` ASC) USING BTREE,
                           INDEX `orders_address_id`(`address_id` ASC) USING BTREE,
                           CONSTRAINT `orders_address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT `orders_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT `orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2025042493594016001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for product_click_stats
-- ----------------------------
DROP TABLE IF EXISTS `product_click_stats`;
CREATE TABLE `product_click_stats`  (
                                        `product_id` int NOT NULL COMMENT '商品ID',
                                        `total_clicks` int NOT NULL DEFAULT 0 COMMENT '总点击次数',
                                        `current_score` int NOT NULL DEFAULT 0 COMMENT '热度评分',
                                        PRIMARY KEY (`product_id`) USING BTREE,
                                        INDEX `idx_current_score`(`current_score` DESC) USING BTREE,
                                        CONSTRAINT `product_click_stats_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_click_stats
-- ----------------------------
INSERT INTO `product_click_stats` VALUES (22203456, 11, 3);
INSERT INTO `product_click_stats` VALUES (22206480, 8, 3);
INSERT INTO `product_click_stats` VALUES (22208623, 8, 3);
INSERT INTO `product_click_stats` VALUES (23230128, 31, 5);
INSERT INTO `product_click_stats` VALUES (23237853, 8, 3);
INSERT INTO `product_click_stats` VALUES (24229569, 9, 3);
INSERT INTO `product_click_stats` VALUES (212231098, 22, 4);
INSERT INTO `product_click_stats` VALUES (212250086, 12, 3);
INSERT INTO `product_click_stats` VALUES (222025167, 9, 3);
INSERT INTO `product_click_stats` VALUES (222033273, 8, 3);
INSERT INTO `product_click_stats` VALUES (222036639, 14, 3);
INSERT INTO `product_click_stats` VALUES (232330154, 8, 3);
INSERT INTO `product_click_stats` VALUES (232334683, 28, 4);
INSERT INTO `product_click_stats` VALUES (232349121, 10, 3);
INSERT INTO `product_click_stats` VALUES (232355002, 9, 3);
INSERT INTO `product_click_stats` VALUES (242215188, 8, 3);
INSERT INTO `product_click_stats` VALUES (242217910, 8, 3);
INSERT INTO `product_click_stats` VALUES (242220564, 11, 3);
INSERT INTO `product_click_stats` VALUES (242228594, 12, 3);
INSERT INTO `product_click_stats` VALUES (242238260, 10, 3);
INSERT INTO `product_click_stats` VALUES (242241246, 8, 3);
INSERT INTO `product_click_stats` VALUES (242249233, 8, 3);
INSERT INTO `product_click_stats` VALUES (242250007, 10, 3);
INSERT INTO `product_click_stats` VALUES (242254175, 8, 3);
INSERT INTO `product_click_stats` VALUES (242256909, 8, 3);
INSERT INTO `product_click_stats` VALUES (242311795, 8, 3);
INSERT INTO `product_click_stats` VALUES (242317759, 8, 3);
INSERT INTO `product_click_stats` VALUES (242319092, 9, 3);
INSERT INTO `product_click_stats` VALUES (242320575, 8, 3);
INSERT INTO `product_click_stats` VALUES (242321882, 9, 3);
INSERT INTO `product_click_stats` VALUES (242330663, 9, 3);
INSERT INTO `product_click_stats` VALUES (242335196, 8, 3);
INSERT INTO `product_click_stats` VALUES (242341028, 8, 3);
INSERT INTO `product_click_stats` VALUES (242346295, 9, 3);
INSERT INTO `product_click_stats` VALUES (242347754, 9, 3);
INSERT INTO `product_click_stats` VALUES (242349201, 8, 3);
INSERT INTO `product_click_stats` VALUES (242351230, 8, 3);
INSERT INTO `product_click_stats` VALUES (242353657, 8, 3);
INSERT INTO `product_click_stats` VALUES (242353811, 10, 3);
INSERT INTO `product_click_stats` VALUES (242356485, 8, 3);
INSERT INTO `product_click_stats` VALUES (242358437, 9, 3);

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
                             `product_id` int NOT NULL COMMENT '商品id',
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品描述',
                             `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
                             `category_id` int NOT NULL COMMENT '类别id',
                             `stock` int NULL DEFAULT 0 COMMENT '库存数量',
                             `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片链接',
                             `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发布人用户名',
                             `is_sell` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否上架（0，1）',
                             `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
                             PRIMARY KEY (`product_id` DESC) USING BTREE,
                             INDEX `category_id`(`category_id` ASC) USING BTREE,
                             INDEX `products_user_username`(`username` ASC) USING BTREE,
                             CONSTRAINT `products_category_id` FOREIGN KEY (`category_id`) REFERENCES `products_categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT `products_user_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (242358437, '足金花型戒指', '足金材质，戒面雕琢精致花型图案，造型饱满，细节丰富，尽显华贵，散发复古优雅气息。', 10999.00, 28, 1, 'http://aweqy.asia/JewelryPurchase/api/image/02c7a6a9-0983-4bc8-a923-27ca9e3ae43a.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242356485, '玫瑰金珍珠耳钉', '圆润珍珠搭配玫瑰金，珍珠散发柔和光晕，周围玫瑰金装饰精巧细腻，优雅复古，尽显温婉气质。', 3600.00, 24, 2, 'http://aweqy.asia/JewelryPurchase/api/image/e84c0e80-4747-45b3-a837-4c5f634473e2.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242353811, '繁华足金手镯', '足金打造，镯身精雕细腻花卉图案，花朵栩栩如生，叶片脉络清晰，工艺精湛，尽显华贵大气，寓意美好。', 6888.00, 26, 2, 'http://aweqy.asia/JewelryPurchase/api/image/21145bb9-cf65-4af2-9383-01ea8029aa42.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242353657, '经典单钻戒指', '以纯净白金打造简约戒托，镶嵌一颗璀璨圆形钻石，切割工艺精湛，火彩夺目，经典款式诠释永恒爱意。', 9999.00, 28, 1, 'http://aweqy.asia/JewelryPurchase/api/image/95caec8a-3cc6-441e-9fba-67474ef85d1c.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242351230, '红宝奢华戒指', '甄选色泽浓郁的椭圆形红宝石为主石，搭配玫瑰金戒托，侧边镶嵌精致小钻，高贵典雅，尽显奢华魅力。', 5888.00, 28, 1, 'http://aweqy.asia/JewelryPurchase/api/image/495a16c9-d7bd-40d4-8626-cf2c55556d76.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242349201, '灵动钻饰戒指', '白金戒环流畅优雅，主钻周围环绕精致辅钻，独特设计使钻石更显灵动闪耀，展现精致时尚品味。', 8999.00, 28, 2, 'http://aweqy.asia/JewelryPurchase/api/image/8191124e-519f-4809-9a0a-79bb5bb8e9c0.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242347754, '群镶美钻戒指', '白金戒托上主钻被群镶小钻簇拥，形成华丽视觉效果，光芒四射，彰显高贵与浪漫。', 9699.00, 28, 2, 'http://aweqy.asia/JewelryPurchase/api/image/676b6c68-7762-44ca-8b04-f6bcff9bf1bb.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242346295, '龙凤足金手镯', '以足金为材，精心雕琢龙凤图案，龙凤姿态矫健，栩栩如生，寓意吉祥，工艺精湛，彰显传统祥瑞文化。', 7888.00, 26, 2, 'http://aweqy.asia/JewelryPurchase/api/image/cbd5dd05-762a-4a5a-a53d-1662c0b66eb7.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242341028, '绿宝璀璨项链', '玫瑰金链条精致细腻，方形绿宝石浓郁深邃，周围镶嵌闪亮钻石，层次分明，尽显高贵奢华。', 6666.00, 19, 2, 'http://aweqy.asia/JewelryPurchase/api/image/9494bc22-9df6-4912-95d8-4272910c1f7d.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242335196, '紫罗兰翡翠手镯', '呈现迷人淡紫色，色泽柔和均匀，质地细腻光滑，宛如春日紫霞，尽显温婉浪漫气质，是优雅品味之选。', 6888.00, 26, 1, 'http://aweqy.asia/JewelryPurchase/api/image/53d85131-fd8d-46da-ad8b-05c56509860f.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242330663, '飘蓝花翡翠手镯', '翡翠质地通透，蓝花似幽潭水草飘逸其中，纹理自然流畅，低调中蕴含独特美感，散发淡雅沉静气质。', 3699.00, 26, 2, 'http://aweqy.asia/JewelryPurchase/api/image/e80e7a24-a380-4633-afe0-98588e0c3522.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242321882, '黑钻经典项链', '玫瑰金链条搭配经典圆环吊坠，黑色与玫瑰金碰撞，简约中透着时尚大气，经典百搭。', 6999.00, 19, 1, 'http://aweqy.asia/JewelryPurchase/api/image/6d08f2bd-2b60-4038-b798-1784ce3aee02.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242320575, '素圈足金手镯', '简约素圈造型，足金材质色泽亮丽，线条流畅圆润，经典百搭，诠释纯粹之美，尽显低调奢华。', 6666.00, 26, 1, 'http://aweqy.asia/JewelryPurchase/api/image/60d7ef98-0d62-4aa8-b31a-1c0631f7f64c.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242319092, '飘绿翡翠手镯', '精选翡翠材质，色泽温润，翠色如灵动墨韵自然晕染，质地细腻，水头足，尽显东方古典韵味，优雅别致。', 5888.00, 26, 2, 'http://aweqy.asia/JewelryPurchase/api/image/3b8ea2a5-39e8-4689-b022-3bdb81ff63d2.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242317759, '红宝爱心项链', '玫瑰金链条质感十足，心形红宝石鲜艳夺目，周围钻石呈心形簇拥，浪漫华丽，爱意满溢。', 9999.00, 19, 2, 'http://aweqy.asia/JewelryPurchase/api/image/db7bb366-0da3-4cb1-a3fc-a97c3f970c74.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242311795, '贝母钻饰项链', '玫瑰金链环绕，圆形贝母散发梦幻光泽，顶部钻石点缀，精致优雅，展现迷人魅力。', 0.00, 19, 3, 'http://aweqy.asia/JewelryPurchase/api/image/deedb543-2498-445b-a1ce-13459ff517fb.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242256909, '几何玫瑰金耳坠', '几何图形为设计灵感，上方菱形框架搭配圆珠，下方金属细条流苏，造型新颖独特，时尚且富有艺术感。', 1999.00, 25, 4, 'http://aweqy.asia/JewelryPurchase/api/image/500ad44f-f4b5-44e1-a4e0-85eca8d1c921.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242254175, '星星玫瑰金耳坠', '主体为星星造型，表面独特车花工艺，立体感十足。搭配玫瑰金流苏，星星元素俏皮可爱，走动间流苏轻晃，时尚感拉满。', 2000.00, 25, 4, 'http://aweqy.asia/JewelryPurchase/api/image/3c14d914-66df-46e2-9022-fe861a957fbc.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242250007, '玫瑰金方形闪钻耳钉', '玫瑰金打造方形轮廓，表面镶嵌密集闪钻，光泽迷人，简约大方，为造型增添闪耀亮点，适配多种风格。', 1688.00, 24, 3, 'http://aweqy.asia/JewelryPurchase/api/image/2846a3b7-73c5-462b-80db-7f1598553faf.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242249233, '玫瑰金流苏耳坠', '采用玫瑰金材质，链条与圆珠巧妙组合成流苏造型，线条流畅，摇曳生姿，简约中透露着时尚韵味，优雅气质自然流露。', 1800.00, 25, 2, 'http://aweqy.asia/JewelryPurchase/api/image/767d2ab8-94ec-42a0-9c7c-70735b42afae.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242241246, '花朵钻石耳钉', '以小巧花朵为造型，精致镶嵌闪耀钻石，银色金属勾勒花瓣轮廓，璀璨夺目，尽显灵动甜美，为耳畔增添优雅浪漫气息。 ', 2000.00, 24, 3, 'http://aweqy.asia/JewelryPurchase/api/image/7e419232-b35c-486c-a125-a0182658ea5b.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242238260, '绿宝石放射状耳钉', '以金色为基底，呈放射状造型，璀璨钻石环绕镶嵌，中央镶嵌一颗浓郁绿宝石，如春日新芽，精巧别致，奢华闪耀，彰显独特品味。', 3688.00, 24, 4, 'http://aweqy.asia/JewelryPurchase/api/image/1855f8db-fcf8-45cb-94d5-92e15e17585d.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242228594, '绿宝石方形镂空耳钉', '金色方形框架，采用镂空工艺，精致钻石与中央绿宝石交相辉映，古典中透着时尚，尽显优雅格调。', 999.00, 24, 7, 'http://aweqy.asia/JewelryPurchase/api/image/529235b9-6d55-4d0a-aa37-8ee6206bd3db.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242220564, '椭圆形绿宝石吊坠', '精选椭圆形绿宝石，质地温润，绿意盈盈。镶嵌于精巧的玫瑰金托之上，顶部饰以别致造型与碎钻，设计精巧，彰显优雅气质。', 3600.00, 23, 5, 'http://aweqy.asia/JewelryPurchase/api/image/4e4f7d1c-ef29-468c-9098-bebb113e8e85.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242217910, '翡翠竹节吊坠', '以翡翠雕琢竹节造型，色泽清新淡雅，质地细腻温润。竹节线条流畅自然，寓意节节高升，古朴雅致中蕴含美好祝福。', 5800.00, 15, 1, 'http://aweqy.asia/JewelryPurchase/api/image/ca41b05e-0b77-43a5-9240-97fdabfcebe6.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (242215188, '翡翠如意吊坠', '采用优质翡翠，整体呈如意形状，雕刻线条柔和流畅。翡翠色泽均匀，水头足，如意造型寓意吉祥如意，温润中透着灵动之美。', 4800.00, 15, 1, 'http://aweqy.asia/JewelryPurchase/api/image/11c40395-fbb2-4860-8fde-90a71bf66f41.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (232355002, '多宝银饰手串', '以灰绿色玉石圆珠为主体，搭配多样银质配件，有条纹造型、葫芦形等，工艺精湛。彩色珐琅元素添彩，“招财”圆牌、平安扣、祥云及“福”字银牌等坠饰灵动，寓意招财纳福、平安吉祥 。', 2000.00, 22, 6, 'http://aweqy.asia/JewelryPurchase/api/image/292fddb7-f246-43c1-80ea-51d30d0b3393.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (232349121, '四叶草吊坠项链', '四叶草造型足金吊坠，叶片纹理精致，以细腻工艺勾勒轮廓，挂于简约金链之上，象征幸运美好，散发精致魅力。', 7800.00, 12, 5, 'http://aweqy.asia/JewelryPurchase/api/image/d6dc0a91-4784-401a-962c-5ef094d2e9fe.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (232334683, '玉石银饰手串', '精选温润灰绿色玉石圆珠串联，搭配古朴银质隔珠与饰件，如意纹、福字等元素精巧点缀。更有黄色蜜蜡饼子增添亮色，下方“如意”银牌与玉石如意锁形坠饰呼应，寓意事事顺遂，古典雅致。', 2000.00, 22, 4, 'http://aweqy.asia/JewelryPurchase/api/image/e5be4223-df92-4db3-9226-e88273003079.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (232330154, '双环流苏吊坠项链', '金链设计独特，融合不同链型，搭配圆筒形与圆形吊坠，流苏造型灵动，彰显时尚个性，为穿搭增添层次感。', 6400.00, 12, 3, 'http://aweqy.asia/JewelryPurchase/api/image/ee386b95-8e51-4c9c-a97d-739564e61c93.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (222036639, '无事牌吊坠', '长方形玉石无事牌，简洁大方。顶部绿宝石与碎钻点缀，尽显低调奢华，象征平安顺遂。', 8499.00, 21, 2, 'http://aweqy.asia/JewelryPurchase/api/image/0d1d3a4c-51d3-453b-a2bb-ab47356a5a11.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (222033273, '四叶草吊坠', '四叶草形状玉石，被精致银边与钻石环绕，浪漫精巧，寓意幸运常伴。', 5600.00, 20, 10, 'http://aweqy.asia/JewelryPurchase/api/image/212ee7e7-f1f4-4a4e-8386-ffef80c367c3.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (222025167, '水滴吊坠', '水滴形玉石纯净优雅，顶部镶嵌红宝石与碎钻，红与白的碰撞，色彩鲜明，设计简约大方又不失高贵。', 7800.00, 20, 6, 'http://aweqy.asia/JewelryPurchase/api/image/15cc711c-e56b-4dc9-a098-79d03d436596.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (212250086, '平安扣吊坠', '温润玉石雕琢成平安扣形状，寓意平安圆满。搭配精致银链，上方镶嵌小巧绿宝石与闪耀碎钻，为经典款式增添灵动与华丽', 5000.00, 20, 4, 'http://aweqy.asia/JewelryPurchase/api/image/0b88151d-a993-4690-94da-822640e7c5a1.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (212231098, '佛公吊坠', '玉石精心雕刻成弥勒佛形象，笑容慈祥。银质镶边配以璀璨钻石，既显庄重又具时尚感，传递豁达喜乐之意', 4500.00, 20, 7, 'http://aweqy.asia/JewelryPurchase/api/image/c0cccc84-2978-43c6-9471-0a5c066cf699.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (24229569, '矩形绿宝石吊坠', '主体为矩形切割的浓郁绿宝石，色泽鲜艳，翠意夺目。以精致玫瑰金勾勒轮廓，搭配闪耀碎钻点缀四周，尽显奢华精致，高贵典雅。', 3500.00, 23, 4, 'http://aweqy.asia/JewelryPurchase/api/image/dcfad2b8-89ee-48ac-89cd-f2fcb2649126.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (23237853, '圆牌吊坠项链', '足金打造的圆牌吊坠，外层以精致镂空工艺呈现，内层圆面饰有细腻纹理，搭配简约金链，链间点缀圆珠，尽显复古优雅，寓意生活圆满。', 6600.00, 12, 2, 'http://aweqy.asia/JewelryPurchase/api/image/d30d0cb1-597a-435c-be00-eb3247ec7398.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (23230128, '钥匙吊坠', '以钥匙为造型，主体为银质，钥匙头部呈四叶草轮廓，镶嵌闪耀钻石，中央镶嵌圆润绿宝石，精巧别致，寓意开启幸运之门。', 4500.00, 20, 3, 'http://aweqy.asia/JewelryPurchase/api/image/8da1954b-f9e3-4aea-8bbd-c56e2af6153e.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (22208623, '玉牌吊坠', '玉石玉牌造型，质感温润。上方绿宝石与银质花纹装饰，古朴中透露时尚，寓意美好。', 5300.00, 20, 6, 'http://aweqy.asia/JewelryPurchase/api/image/8a517c68-983f-4d16-9589-e4f6452a0c9b.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (22206480, '宝瓶吊坠', '玉石宝瓶造型别致，线条柔和。瓶颈处银质蝴蝶结装饰，搭配双钻坠饰，精巧可爱，寓意福气满满、平平安安。', 5600.00, 20, 7, 'http://aweqy.asia/JewelryPurchase/api/image/9e06aa16-b9bc-4143-99b0-e718b9ac146d.png', 'patience', '1', 0);
INSERT INTO `products` VALUES (22203456, '福袋吊坠', '玉石福袋造型，饱满圆润。银质双心与蝴蝶结装饰，镶嵌碎钻，可爱又闪耀，寓意福运满满。', 6300.00, 20, 12, 'http://aweqy.asia/JewelryPurchase/api/image/9bfbf321-5dec-467f-a72f-f7a847b41346.png', 'patience', '1', 0);

-- ----------------------------
-- Table structure for products_categories
-- ----------------------------
DROP TABLE IF EXISTS `products_categories`;
CREATE TABLE `products_categories`  (
                                        `category_id` int NOT NULL AUTO_INCREMENT COMMENT '类别ID',
                                        `categories_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名称',
                                        PRIMARY KEY (`category_id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of products_categories
-- ----------------------------
INSERT INTO `products_categories` VALUES (28, '戒指');
INSERT INTO `products_categories` VALUES (26, '手镯');
INSERT INTO `products_categories` VALUES (25, '耳坠');
INSERT INTO `products_categories` VALUES (24, '耳钉');
INSERT INTO `products_categories` VALUES (23, '宝石镶嵌类');
INSERT INTO `products_categories` VALUES (22, '手串');
INSERT INTO `products_categories` VALUES (21, '银镶嵌吊坠');
INSERT INTO `products_categories` VALUES (20, '吊坠');
INSERT INTO `products_categories` VALUES (19, '项链');
INSERT INTO `products_categories` VALUES (15, '翡翠');
INSERT INTO `products_categories` VALUES (12, '黄金');
INSERT INTO `products_categories` VALUES (1, '其他');

-- ----------------------------
-- Table structure for products_image
-- ----------------------------
DROP TABLE IF EXISTS `products_image`;
CREATE TABLE `products_image`  (
                                   `image_id` int NOT NULL AUTO_INCREMENT COMMENT '图片id',
                                   `product_id` int NOT NULL COMMENT '商品id',
                                   `image_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图片链接',
                                   PRIMARY KEY (`image_id`) USING BTREE,
                                   INDEX `product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of products_image
-- ----------------------------
INSERT INTO `products_image` VALUES (139, 152311328, 'http://aweqy.asia/JewelryPurchase/api/image/7ea8a2c9-3d7d-4df8-96b9-aa96bca2f33c.png');
INSERT INTO `products_image` VALUES (146, 21210372, 'http://aweqy.asia/JewelryPurchase/api/image/2d98e7fd-f1ab-446b-8d31-bbcfeeab2347.png');
INSERT INTO `products_image` VALUES (147, 212250086, 'http://aweqy.asia/JewelryPurchase/api/image/0b88151d-a993-4690-94da-822640e7c5a1.png');
INSERT INTO `products_image` VALUES (148, 212231098, 'http://aweqy.asia/JewelryPurchase/api/image/c0cccc84-2978-43c6-9471-0a5c066cf699.png');
INSERT INTO `products_image` VALUES (149, 212213777, 'http://aweqy.asia/JewelryPurchase/api/image/48a60443-5580-4d7f-85ce-3635c33ea55b.png');
INSERT INTO `products_image` VALUES (150, 212213777, 'http://aweqy.asia/JewelryPurchase/api/image/6dcc37e7-ced0-4171-ab92-9c3bbf98d3bf.png');
INSERT INTO `products_image` VALUES (151, 212213777, 'http://aweqy.asia/JewelryPurchase/api/image/bcb838c6-9a15-4553-81d5-d1e7bbf3fbba.png');
INSERT INTO `products_image` VALUES (152, 212213777, 'http://aweqy.asia/JewelryPurchase/api/image/bfe6277d-75f7-41dd-a46d-ee14811970ac.png');
INSERT INTO `products_image` VALUES (153, 212213777, 'http://aweqy.asia/JewelryPurchase/api/image/4580530d-80b5-4e16-91da-f797b1a4d2e2.png');
INSERT INTO `products_image` VALUES (154, 212213777, 'http://aweqy.asia/JewelryPurchase/api/image/ab30d1ad-1940-4fd7-aae8-4644544cddc8.png');
INSERT INTO `products_image` VALUES (155, 22206480, 'http://aweqy.asia/JewelryPurchase/api/image/9e06aa16-b9bc-4143-99b0-e718b9ac146d.png');
INSERT INTO `products_image` VALUES (156, 222025167, 'http://aweqy.asia/JewelryPurchase/api/image/15cc711c-e56b-4dc9-a098-79d03d436596.png');
INSERT INTO `products_image` VALUES (157, 222036639, 'http://aweqy.asia/JewelryPurchase/api/image/0d1d3a4c-51d3-453b-a2bb-ab47356a5a11.png');
INSERT INTO `products_image` VALUES (158, 222033273, 'http://aweqy.asia/JewelryPurchase/api/image/212ee7e7-f1f4-4a4e-8386-ffef80c367c3.png');
INSERT INTO `products_image` VALUES (159, 22203456, 'http://aweqy.asia/JewelryPurchase/api/image/9bfbf321-5dec-467f-a72f-f7a847b41346.png');
INSERT INTO `products_image` VALUES (160, 22208623, 'http://aweqy.asia/JewelryPurchase/api/image/8a517c68-983f-4d16-9589-e4f6452a0c9b.png');
INSERT INTO `products_image` VALUES (161, 23230128, 'http://aweqy.asia/JewelryPurchase/api/image/8da1954b-f9e3-4aea-8bbd-c56e2af6153e.png');
INSERT INTO `products_image` VALUES (162, 232334543, 'http://aweqy.asia/JewelryPurchase/api/image/ce361181-36a4-4c9b-b7f1-b7da060c5bbf.png');
INSERT INTO `products_image` VALUES (163, 23237853, 'http://aweqy.asia/JewelryPurchase/api/image/d30d0cb1-597a-435c-be00-eb3247ec7398.png');
INSERT INTO `products_image` VALUES (164, 232330154, 'http://aweqy.asia/JewelryPurchase/api/image/ee386b95-8e51-4c9c-a97d-739564e61c93.png');
INSERT INTO `products_image` VALUES (165, 232349121, 'http://aweqy.asia/JewelryPurchase/api/image/d6dc0a91-4784-401a-962c-5ef094d2e9fe.png');
INSERT INTO `products_image` VALUES (166, 232334683, 'http://aweqy.asia/JewelryPurchase/api/image/e5be4223-df92-4db3-9226-e88273003079.png');
INSERT INTO `products_image` VALUES (167, 232355002, 'http://aweqy.asia/JewelryPurchase/api/image/292fddb7-f246-43c1-80ea-51d30d0b3393.png');
INSERT INTO `products_image` VALUES (168, 24229569, 'http://aweqy.asia/JewelryPurchase/api/image/dcfad2b8-89ee-48ac-89cd-f2fcb2649126.png');
INSERT INTO `products_image` VALUES (169, 242220564, 'http://aweqy.asia/JewelryPurchase/api/image/4e4f7d1c-ef29-468c-9098-bebb113e8e85.png');
INSERT INTO `products_image` VALUES (170, 242217910, 'http://aweqy.asia/JewelryPurchase/api/image/ca41b05e-0b77-43a5-9240-97fdabfcebe6.png');
INSERT INTO `products_image` VALUES (171, 242215188, 'http://aweqy.asia/JewelryPurchase/api/image/11c40395-fbb2-4860-8fde-90a71bf66f41.png');
INSERT INTO `products_image` VALUES (172, 242241246, 'http://aweqy.asia/JewelryPurchase/api/image/7e419232-b35c-486c-a125-a0182658ea5b.png');
INSERT INTO `products_image` VALUES (173, 242249233, 'http://aweqy.asia/JewelryPurchase/api/image/767d2ab8-94ec-42a0-9c7c-70735b42afae.png');
INSERT INTO `products_image` VALUES (174, 242254175, 'http://aweqy.asia/JewelryPurchase/api/image/3c14d914-66df-46e2-9022-fe861a957fbc.png');
INSERT INTO `products_image` VALUES (175, 242256909, 'http://aweqy.asia/JewelryPurchase/api/image/500ad44f-f4b5-44e1-a4e0-85eca8d1c921.png');
INSERT INTO `products_image` VALUES (176, 242238260, 'http://aweqy.asia/JewelryPurchase/api/image/1855f8db-fcf8-45cb-94d5-92e15e17585d.png');
INSERT INTO `products_image` VALUES (177, 242228594, 'http://aweqy.asia/JewelryPurchase/api/image/529235b9-6d55-4d0a-aa37-8ee6206bd3db.png');
INSERT INTO `products_image` VALUES (178, 242250007, 'http://aweqy.asia/JewelryPurchase/api/image/2846a3b7-73c5-462b-80db-7f1598553faf.png');
INSERT INTO `products_image` VALUES (179, 242356485, 'http://aweqy.asia/JewelryPurchase/api/image/e84c0e80-4747-45b3-a837-4c5f634473e2.png');
INSERT INTO `products_image` VALUES (180, 242319092, 'http://aweqy.asia/JewelryPurchase/api/image/3b8ea2a5-39e8-4689-b022-3bdb81ff63d2.png');
INSERT INTO `products_image` VALUES (181, 242335196, 'http://aweqy.asia/JewelryPurchase/api/image/53d85131-fd8d-46da-ad8b-05c56509860f.png');
INSERT INTO `products_image` VALUES (182, 242330663, 'http://aweqy.asia/JewelryPurchase/api/image/e80e7a24-a380-4633-afe0-98588e0c3522.png');
INSERT INTO `products_image` VALUES (183, 242353811, 'http://aweqy.asia/JewelryPurchase/api/image/21145bb9-cf65-4af2-9383-01ea8029aa42.png');
INSERT INTO `products_image` VALUES (184, 242320575, 'http://aweqy.asia/JewelryPurchase/api/image/60d7ef98-0d62-4aa8-b31a-1c0631f7f64c.png');
INSERT INTO `products_image` VALUES (185, 242346295, 'http://aweqy.asia/JewelryPurchase/api/image/cbd5dd05-762a-4a5a-a53d-1662c0b66eb7.png');
INSERT INTO `products_image` VALUES (186, 242351230, 'http://aweqy.asia/JewelryPurchase/api/image/495a16c9-d7bd-40d4-8626-cf2c55556d76.png');
INSERT INTO `products_image` VALUES (187, 242353657, 'http://aweqy.asia/JewelryPurchase/api/image/95caec8a-3cc6-441e-9fba-67474ef85d1c.png');
INSERT INTO `products_image` VALUES (188, 242349201, 'http://aweqy.asia/JewelryPurchase/api/image/8191124e-519f-4809-9a0a-79bb5bb8e9c0.png');
INSERT INTO `products_image` VALUES (189, 242347754, 'http://aweqy.asia/JewelryPurchase/api/image/676b6c68-7762-44ca-8b04-f6bcff9bf1bb.png');
INSERT INTO `products_image` VALUES (190, 242358437, 'http://aweqy.asia/JewelryPurchase/api/image/02c7a6a9-0983-4bc8-a923-27ca9e3ae43a.png');
INSERT INTO `products_image` VALUES (191, 242341028, 'http://aweqy.asia/JewelryPurchase/api/image/9494bc22-9df6-4912-95d8-4272910c1f7d.png');
INSERT INTO `products_image` VALUES (192, 242321882, 'http://aweqy.asia/JewelryPurchase/api/image/6d08f2bd-2b60-4038-b798-1784ce3aee02.png');
INSERT INTO `products_image` VALUES (193, 242311795, 'http://aweqy.asia/JewelryPurchase/api/image/deedb543-2498-445b-a1ce-13459ff517fb.png');
INSERT INTO `products_image` VALUES (194, 242317759, 'http://aweqy.asia/JewelryPurchase/api/image/db7bb366-0da3-4cb1-a3fc-a97c3f970c74.png');

-- ----------------------------
-- Table structure for user_similarities
-- ----------------------------
DROP TABLE IF EXISTS `user_similarities`;
CREATE TABLE `user_similarities`  (
                                      `user_id` int NOT NULL COMMENT '用户ID',
                                      `similar_user_id` int NOT NULL COMMENT '相似用户ID',
                                      `similarity_score` decimal(10, 9) NOT NULL COMMENT '相似度',
                                      PRIMARY KEY (`user_id`, `similar_user_id`) USING BTREE,
                                      INDEX `similar_user_id`(`similar_user_id` ASC) USING BTREE,
                                      CONSTRAINT `user_similarities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                      CONSTRAINT `user_similarities_ibfk_2` FOREIGN KEY (`similar_user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_similarities
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                          `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
                          `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'MD5密码',
                          `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册手机号',
                          PRIMARY KEY (`user_id`) USING BTREE,
                          UNIQUE INDEX `username`(`username` ASC) USING BTREE,
                          UNIQUE INDEX `username_2`(`username` ASC) USING BTREE,
                          UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (22, '不会吸猫', '25d55ad283aa400af464c76d713c07ad', '17752515074');
INSERT INTO `users` VALUES (29, 'patience', '25d55ad283aa400af464c76d713c07ad', '13820149668');

SET FOREIGN_KEY_CHECKS = 1;
