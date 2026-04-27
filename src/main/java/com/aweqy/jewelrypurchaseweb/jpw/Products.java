package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("商品ID")
    @Column(name = "product_id")
    private int id;

    @OneToOne  // 或 @ManyToOne 根据实际关系
    @JoinColumn(name = "product_id", nullable = false)
    private ProductClickStats clickStats;

    @Comment("商品名称")
    @Column(name = "name")
    private String name;

    @Comment("商品描述")
    @Column(name = "description")
    private String description;

    @Comment("商品价格")
    @Column(name = "price", precision = 10, scale = 9)
    private BigDecimal price;

    @Comment("商品类别")
    @Column(name = "category_id", nullable = false)
    private int categoryId;

    @Comment("商品库存")
    @Column(name = "stock")
    private int stock;

    @Comment("商品主图")
    @Column(name = "image_url")
    private String imageUrl;

    @Comment("发布者")
    @Column(name = "username", nullable = false)
    private String author;

    @Comment("是否上架商品")
    @Column(name = "is_sell", nullable = false)
    private Integer isSell;

    @Comment("乐观锁版本")
    @Column(name = "version", nullable = false)
    private Integer version;

    public Products(int id, String name, String description, BigDecimal price, int categoryId, int stock, String imageUrl, String author, Integer isSell, Integer version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.author = author;
        this.isSell = isSell;
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Products() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductClickStats getClickStats() {
        return clickStats;
    }

    public void setClickStats(ProductClickStats clickStats) {
        this.clickStats = clickStats;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getIsSell() {
        return isSell;
    }

    public void setIsSell(Integer isSell) {
        this.isSell = isSell;
    }
}
