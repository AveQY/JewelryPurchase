# JewelryPurchaseWeb

珠宝购后端服务，基于 Spring Boot 3 + JPA + MySQL 实现，面向珠宝电商场景提供用户、商品、购物车、订单、地址、图片上传、轮播图与个性化推荐等接口。

## 项目概览

该项目是 `JewelryPurchase` 体系中的后端 API 服务，启动后默认通过 `http://localhost:6601/api/` 提供接口。

从代码结构和数据库脚本来看，项目覆盖了一个完整的珠宝交易基础流程：

- 用户注册、登录、Token 生成与解析
- 商品查询、搜索、分类与库存扣减
- 购物车管理
- 收货地址管理
- 订单创建、状态更新与删除
- 商品图片上传与访问
- 首页轮播图与推荐商品展示
- 基于用户购买行为的协同过滤推荐
- 基于点击热度的商品排序衰减机制

## 技术栈

### 后端框架

- Spring Boot 3.4.1
- Spring Web
- Spring Data JPA / Hibernate
- HikariCP

### 数据与认证

- MySQL 8+
- JWT

### 其他依赖

- Lombok
- org.json
- JAXB API

### Java 版本

- Java 17

## 目录结构

```text
src/main/java/com/aweqy/jewelrypurchaseweb/
├─ controller/   # 接口控制层
├─ Dao/          # JPA Repository 数据访问层
├─ jpw/          # 实体、枚举、统一返回对象
├─ service/      # 业务逻辑层
├─ Util/         # JWT、订单号等工具类
└─ JewelryPurchaseWebApplication.java

src/main/resources/
├─ application.properties
└─ db/migration/
   ├─ V1__Create_table.sql
   └─ V1__init_table.sql
```

## 核心模块说明

### 1. 用户与认证

相关文件：

- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/UserController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/UserService.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/Util/JwtUtil.java`

提供能力：

- 用户注册
- 用户登录
- 查询用户
- JWT 生成与解析

说明：

- `JwtUtil` 使用固定密钥生成 Token，有效期约 1 年。
- 当前登录、注册接口使用 `GET` 方式传参，适合课程项目或演示环境，生产环境建议改为 `POST` 并避免明文查询参数传递敏感信息。

### 2. 商品与分类

相关文件：

- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/ProductController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/ProductService.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/Dao/ProductRepository.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/Dao/ProductCategoryRepository.java`

提供能力：

- 首页推荐商品分页查询
- 商品详情查询
- 商品名称模糊搜索
- 商品分类查询 / 创建
- 商品库存扣减

实现特点：

- 商品热度通过 `product_click_stats` 维护。
- 商品详情查询时会累计点击量与热度分值。
- 库存扣减使用 `version` 字段配合重试机制，带有乐观锁思想，适合并发下单场景。

### 3. 个性化推荐

相关文件：

- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/RecommendationController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/CollaborativeFilteringService.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/ScoreDecayService.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/Dao/UserSimilarityRepository.java`

提供能力：

- 基于用户历史购买记录的个性化推荐
- 相似用户关系计算与持久化
- 热度分值定时衰减

实现特点：

- 使用余弦相似度计算用户之间的相似性。
- 当相似用户不足或无有效推荐结果时，自动回退到热门商品推荐。
- `CollaborativeFilteringService` 中包含定时计算用户相似度的方法。
- `ScoreDecayService` 每周一凌晨 3 点衰减一次商品热度分值。

### 4. 购物车与订单

相关文件：

- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/DashboardController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/OrderController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/DashboardService.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/OrderService.java`

提供能力：

- 购物车商品添加、查询、删除
- 订单创建、查询、更新、删除
- 支付方式与订单状态枚举映射

实现特点：

- 下单时会生成订单号。
- 创建订单后会重新触发用户相似度计算，以更新推荐结果。

### 5. 商家库存与上架管理

相关文件：

- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/InventoryController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/service/InventoryService.java`

提供能力：

- 商家商品库存查询
- 商品新增
- 商品删除
- 商品上下架状态修改

说明：

- 当前部分接口通过解析 Token 字符串中的 `sub` 字段获取用户名，属于偏手工处理的实现方式，后续可以统一改成拦截器或过滤器做鉴权。

### 6. 地址、轮播图与图片上传

相关文件：

- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/AddressController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/CarouselController.java`
- `src/main/java/com/aweqy/jewelrypurchaseweb/controller/PictureFileController.java`

提供能力：

- 收货地址维护
- 首页轮播图读取
- 商品图片上传、图片访问、商品图片查询

说明：

- 图片上传目录由 `app.upload-dir` 控制。
- 上传成功后会把图片 URL 写入数据库，并通过 `/api/image/{filename}` 提供访问。

## 数据库设计

数据库脚本位置：

- `src/main/resources/db/migration/V1__Create_table.sql`
- `src/main/resources/db/migration/V1__init_table.sql`

主要表包括：

- `users`：用户表
- `address`：收货地址表
- `products`：商品表
- `products_categories`：商品分类表
- `products_image`：商品图片表
- `dashboard`：购物车表
- `orders`：订单表
- `carouselimg`：轮播图表
- `product_click_stats`：商品点击与热度表
- `user_similarities`：用户相似度表

说明：

- `V1__Create_table.sql` 适合初始化表结构。
- `V1__init_table.sql` 除了建表，还包含演示数据，可用于本地快速体验。
- 演示数据中已包含珠宝商品、分类、轮播图等内容。

## 配置说明

配置文件：`src/main/resources/application.properties`

当前默认配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jewelrypurchase
spring.datasource.username=root
spring.datasource.password=123456
server.port=6601
spring.jpa.hibernate.ddl-auto=none
logging.file.name=logs/myapp.log
```

重点配置项：

- `spring.datasource.*`：数据库连接配置
- `server.port=6601`：服务端口
- `app.upload-dir`：图片上传目录
- `spring.jpa.hibernate.ddl-auto=none`：不自动建表，需手动执行 SQL
- `logging.file.name=logs/myapp.log`：日志输出位置

## 快速开始

### 1. 准备环境

请先确保本机具备：

- JDK 17
- Maven 3.9+（或直接使用项目自带 `mvnw` / `mvnw.cmd`）
- MySQL 8+

### 2. 创建数据库

先在 MySQL 中创建数据库：

```sql
CREATE DATABASE jewelrypurchase DEFAULT CHARACTER SET utf8mb4;
```

### 3. 初始化数据表

进入 `src/main/resources/db/migration/` 目录后，按需执行 SQL：

#### 方案 A：只建表

执行：

- `V1__Create_table.sql`

#### 方案 B：建表并导入演示数据

执行：

- `V1__init_table.sql`

> 如果使用 `V1__init_table.sql`，它本身包含 `DROP TABLE IF EXISTS` 和演示数据插入，更适合本地展示与联调。

### 4. 修改本地配置

按你的实际环境修改 `application.properties`：

- 数据库地址
- 用户名 / 密码
- 图片上传目录

尤其注意 Windows 本地调试时：

```properties
app.upload-dir=F:/log/
```

如果你的机器没有 `F:` 盘，需要改成实际存在的目录，例如：

```properties
app.upload-dir=D:/JewelryPurchaseUpload/
```

### 5. 启动项目

#### 方式一：使用 Maven Wrapper

Windows：

```bash
mvnw.cmd spring-boot:run
```

macOS / Linux：

```bash
./mvnw spring-boot:run
```

#### 方式二：直接打包运行

```bash
mvn clean package
java -jar target/JewelryPurchaseWeb-0.0.1-SNAPSHOT.jar
```

### 6. 访问服务

启动后可访问：

- 根接口基址：`http://localhost:6601/api/`

## 典型接口示例

以下仅列举部分接口，便于快速理解项目能力：

### 用户

- `GET /api/login`
- `GET /api/register`
- `GET /api/users`

### 商品

- `GET /api/recommend`
- `GET /api/recommend/personal/{userId}`
- `GET /api/search/product`
- `GET /api/search/productName`
- `POST /api/updateProductStock`

### 购物车 / 订单

- `GET /api/dashboard`
- `POST /api/add/dashboard`
- `GET /api/order`
- `GET /api/orderList`
- `POST /api/add/order`

### 图片

- `POST /api/picture_upload`
- `GET /api/image/{filename}`
- `GET /api/search/productImg`

## 项目亮点

- 电商后端核心链路完整，适合作为课程设计、毕业设计或后端练手项目
- 推荐系统不是纯展示，实际落地了用户相似度计算与推荐回退逻辑
- 商品库存更新考虑了并发场景
- 数据库脚本和演示数据齐全，上手门槛低
- 商品、订单、购物车、上传、推荐等模块拆分清晰

## 当前实现中的注意点

这些点不影响项目阅读和运行，但如果后续要继续打磨，建议优先优化：

1. `application.properties` 中默认账号密码为本地开发值，提交到公开仓库前建议改为环境变量。
2. JWT 密钥当前写死在代码中，建议迁移到配置文件或环境变量。
3. 登录 / 注册接口使用 `GET` 请求，不够规范，建议改为 `POST`。
4. 部分 Token 解析逻辑写在控制器中，可抽取为统一鉴权层。
5. 项目存在定时任务方法，但若要确保调度生效，建议确认是否启用了调度配置。
6. 图片上传路径与线上域名逻辑写在控制器中，后续可配置化处理。

## 适用场景

这个项目适合用来：

- 课程设计 / 毕业设计展示
- Spring Boot + JPA 电商后端练手
- 推荐算法与业务系统结合的示例项目
- 珠宝类商城原型后端服务

## 开发工具建议

- IntelliJ IDEA
- Navicat / DataGrip
- Apifox / Postman

## License

仓库中包含 `LICENSE` 文件，使用前可根据仓库实际授权条款进一步确认。