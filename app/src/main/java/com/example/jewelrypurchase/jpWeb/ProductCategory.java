package com.example.jewelrypurchase.jpWeb;


public class ProductCategory {
    private int categoryId;
    private String categoriesName;
    private boolean selected;

    public ProductCategory() {
    }

    public ProductCategory(int categoryId, String categoriesName) {
        this.categoryId = categoryId;
        this.categoriesName = categoriesName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
