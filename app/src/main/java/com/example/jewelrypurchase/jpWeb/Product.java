package com.example.jewelrypurchase.jpWeb;

public class Product {
    private String id;
    private ProductClickStats clickStats;
    private String name;
    private String price;
    private String imageUrl;
    private String description;
    private String categoryId;
    private String stock;
    private String anthor;
    private String isSell;

    public String getIsSell() {
        return isSell;
    }

    public void setIsSell(String isSell) {
        this.isSell = isSell;
    }

    public ProductClickStats getClickStats() {
        return clickStats;
    }

    public void setClickStats(ProductClickStats clickStats) {
        this.clickStats = clickStats;
    }

    public String getAnthor() {
        return anthor;
    }

    public void setAnthor(String anthor) {
        this.anthor = anthor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Product(String id, String name, String price, String imageUrl, String anthor) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.anthor = anthor;
    }

    public Product(String id, String name, String price, String imageUrl, String stock, String isSell) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.isSell = isSell;
    }

    public Product(String id, String name, String price, String imageUrl, String description, String category_id, String stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.categoryId = category_id;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}