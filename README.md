# 珠宝购 - JewelryPurchase Android 客户端

## 项目简介

`JewelryPurchase` 是“珠宝购”项目的 Android 客户端仓库，面向珠宝商城场景提供首页浏览、商品搜索、分类查看、商品详情、购物车、订单、地址管理、个人中心、商家库存管理等核心交互能力。

这个仓库主要承载 **移动端前端应用**，与后端 API 配合完成完整业务流程。

如果你正在找服务端代码，请注意：

> 本仓库还提供另一个分支：`JewelryPurchaseWeb`
>
> 该分支存放的是 **Spring Boot 后端服务代码**，包含数据库脚本、商品接口、订单接口、图片上传、推荐算法等服务端实现。

你可以在这里查看后端分支：

- `JewelryPurchaseWeb` 分支：<https://github.com/AveQY/JewelryPurchase/tree/JewelryPurchaseWeb>

## 仓库定位

当前 `JewelryPurchase` 分支主要是：

- Android 客户端工程
- UI 页面与交互逻辑
- 与后端接口通信的网络层封装
- 商品展示、订单管理、登录注册等移动端业务流程

而 `JewelryPurchaseWeb` 分支主要是：

- Spring Boot 后端服务
- MySQL / JPA 数据访问
- 用户、商品、订单、地址等接口
- 图片上传与静态访问
- 个性化推荐与热度排序逻辑

也就是说，这个仓库实际包含两部分内容：

| 分支 | 作用 | 技术方向 |
|---|---|---|
| `JewelryPurchase` | Android 客户端 | Java + Android SDK |
| `JewelryPurchaseWeb` | 后端服务端 | Spring Boot + JPA + MySQL |

## 技术栈

### 客户端

- Java
- Android SDK
- XML 布局
- ViewBinding
- Fragment + Activity
- RecyclerView / ViewPager2 / SwipeRefreshLayout
- Retrofit
- OkHttp
- Gson
- Glide
- Gradle Kotlin DSL

### 其他依赖

- Markwon（Markdown 展示）
- Fastjson
- OpenTelemetry 相关依赖

## 开发工具

- Android Studio
- Git

## 项目结构

```text
app/src/main/java/com/example/jewelrypurchase/
├─ adapter/          # 列表、轮播图、购物车、订单等适配器
├─ jpWeb/            # 数据模型、接口地址、返回对象
│  └─ util/          # 分页、文件上传、MD5、状态栏等工具
├─ models/           # 业务辅助类、弹窗与滚动辅助逻辑
└─ ui/
   ├─ home/          # 首页、分类、搜索、商品详情、拍卖等页面
   ├─ dashboard/     # 购物车、订单详情
   ├─ inventory/     # 商家库存管理
   ├─ personCenter/  # 登录、地址管理、订单管理、个人中心
   └─ chat/          # 消息 / 聊天相关页面
```

## 功能概览

从代码结构来看，客户端已经覆盖以下主要能力：

- 首页轮播图展示
- 推荐商品分页加载
- 商品搜索与分类浏览
- 商品详情展示
- 登录 / 注册
- 购物车管理
- 订单查看与订单详情
- 地址管理
- 商家库存管理与商品编辑
- 图片上传相关交互
- 用户协议 / 隐私政策展示

## 关键实现说明

### 1. 接口地址切换

相关文件：

- `app/src/main/java/com/example/jewelrypurchase/jpWeb/WebUrl.java`

当前代码中预置了三类地址：

- Android 模拟器本地调试：`http://10.0.2.2:6601`
- 局域网真机调试：`http://yourIP:6601`
- 已部署服务地址：`http://aweqy.asia/JewelryPurchase`

默认返回的是线上地址，因此：

- **线上体验**：通常无需修改
- **本地联调**：需要先部署后端服务，再把 `getBASE_URL()` 返回值改成对应本地地址

### 2. 页面组织方式

相关文件：

- `app/src/main/java/com/example/jewelrypurchase/ui/MainActivity.java`

项目使用 `MainActivity + Navigation + Fragment` 组织底部主页面，主要入口包括：

- 首页
- 购物车
- 库存
- 消息
- 我的

这种结构适合商城类 App 的多标签页切换场景。

### 3. 首页推荐与轮播图

相关文件：

- `app/src/main/java/com/example/jewelrypurchase/ui/home/HomeFragment.java`

首页实现了：

- 轮播图自动切换
- 推荐商品分页加载
- 下拉刷新与上拉加载
- 搜索入口跳转

并通过网络请求调用后端接口：

- `/api/carousel`
- `/api/recommend`

### 4. 登录与注册

相关文件：

- `app/src/main/java/com/example/jewelrypurchase/ui/personCenter/LoginActivity.java`

登录页支持：

- 手机号 + 用户名 + 密码校验
- 登录失败后尝试注册
- 使用 SharedPreferences 持久化保存 token、username、userId
- 展示用户协议与隐私政策

说明：

- 当前客户端会对密码做 MD5 处理后再发给服务端。
- 首次进入应用若未检测到 token，会跳转到登录页面。

## 环境要求

- Android Studio
- JDK 8+（当前模块源码兼容 Java 8）
- Android SDK Compile 34
- Android 9.0+（`minSdk = 28`）

## 快速开始

### 1. 克隆仓库

```bash
git clone https://github.com/AveQY/JewelryPurchase.git
```

### 2. 打开 Android 项目

使用 Android Studio 打开仓库根目录。

### 3. 配置后端地址

如果你需要本地调试，请修改：

- `app/src/main/java/com/example/jewelrypurchase/jpWeb/WebUrl.java`

将 `getBASE_URL()` 的返回值切换到本地后端地址，例如：

```java
return BASE_URL;
```

> 注意：切换到本地地址前，请先确保后端服务已启动，否则客户端接口无法正常访问。

### 4. 运行项目

连接模拟器或真机后，直接在 Android Studio 中运行即可。

## 调试说明

### 线下调试

适用于你本地已经启动后端服务的情况。

推荐做法：

1. 启动 `JewelryPurchaseWeb` 分支对应的 Spring Boot 服务
2. 创建并初始化 MySQL 数据库
3. 修改 `WebUrl.java` 中的返回地址
4. 重新运行 Android 客户端

### 线上调试

如果直接使用已部署地址，通常无须修改配置。

## 下载体验

当前 README 中保留了 APK 下载地址：

- [app-releaseV1.0.5.apk](https://github.com/AveQY/JewelryPurchase/releases/download/V1.0.5/app-release.apk)

## 适合谁看

这个仓库适合以下场景：

- 展示 Android 电商客户端项目
- 课程设计 / 毕业设计成果展示
- Android 与 Spring Boot 前后端联调示例
- 作为珠宝商城类 App 原型项目参考

## 建议阅读顺序

如果你第一次看这个仓库，建议按下面顺序阅读：

1. 当前分支 `JewelryPurchase`：先了解 Android 客户端结构
2. `app/src/main/java/com/example/jewelrypurchase/ui/`：看页面组织方式
3. `app/src/main/java/com/example/jewelrypurchase/jpWeb/WebUrl.java`：看前后端连接方式
4. `JewelryPurchaseWeb` 分支：再查看服务端实现

## 分支说明

### `JewelryPurchase`

Android 客户端主分支，负责页面与交互。

### `JewelryPurchaseWeb`

后端服务分支，负责接口、数据库和业务逻辑。

如果你希望完整理解这个项目，建议两个分支结合起来看：

- 当前分支看“客户端如何调用接口”
- `JewelryPurchaseWeb` 分支看“接口如何实现与返回数据”

## License

仓库中包含 `LICENSE` 文件，具体使用方式可根据项目实际授权条款进一步确认。
