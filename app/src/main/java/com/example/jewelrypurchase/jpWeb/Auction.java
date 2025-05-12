package com.example.jewelrypurchase.jpWeb;

import java.io.Serializable;

public class Auction implements Serializable {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
    private int categoryId;
    private String stock;
    private String startingPrice;
    private String endTime;
    private String author;

    public Auction(int id, String name, String imageUrl, String stock, String endTime, String author) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.endTime = endTime;
        this.author = author;
    }

    public Auction() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
