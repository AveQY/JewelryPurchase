package com.aweqy.jewelrypurchaseweb.jpw;


import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "carouselimg")
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("轮播图ID")
    @Column(name = "id")
    private int id;

    @Lob
    @Comment("轮播图Url")
    @Column(name = "image_url", columnDefinition = "longtext")
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