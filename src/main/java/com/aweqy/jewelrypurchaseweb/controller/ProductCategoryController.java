package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.jpw.ProductCategory;
import com.aweqy.jewelrypurchaseweb.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/search/all/productCategory")
    public List<ProductCategory> getAllProductCategory() {
        return productCategoryService.getAllProductCategory();
    }

    @GetMapping("/search/product/category")
    public ResponseEntity<?> getProductCategoryByCategoryId(@RequestParam int categoryId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "8") int size) {
        try {
            Page<ProductCategory> productCategoryPage = productCategoryService.getProductCategoryByCategoryId(categoryId, page, size);
            return ResponseEntity.ok(productCategoryPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取商品失败: " + e.getMessage()));
        }
    }
}
