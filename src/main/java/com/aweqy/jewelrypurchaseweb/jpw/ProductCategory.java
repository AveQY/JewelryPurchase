package com.aweqy.jewelrypurchaseweb.jpw;

import jakarta.persistence.*;

@Entity
@Table(name = "products_categories")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "categories_name")
    private String categoriesName;

    public ProductCategory(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public ProductCategory() {

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }
}
