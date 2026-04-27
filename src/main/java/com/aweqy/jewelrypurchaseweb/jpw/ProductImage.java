package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;

@Entity
@Table(name = "products_image")
public class ProductImage {
    @Id
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "product_id")
    private int productId;

    @Lob
    @Column(name = "image_url", columnDefinition = "longtext")
    private String imageUrl;

    public ProductImage(int product_id, String image_url) {
        this.productId = product_id;
        this.imageUrl = image_url;
    }

    public ProductImage() {

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int image_id) {
        this.imageId = image_id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int product_id) {
        this.productId = product_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
