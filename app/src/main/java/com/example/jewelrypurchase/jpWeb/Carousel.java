package com.example.jewelrypurchase.jpWeb;

public class Carousel {

    private int id;

    private String imageUrl;

    public Carousel(int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public Carousel() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
