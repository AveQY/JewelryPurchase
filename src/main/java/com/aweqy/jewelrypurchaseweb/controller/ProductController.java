package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.jpw.ProductCategory;
import com.aweqy.jewelrypurchaseweb.jpw.Products;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/api/recommend")
    public ResponseEntity<?> getRecommendingCommodities(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "16") int size) {
        try {
            Page<Products> result = productService.sFindRecommendingCommodities(page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取商品失败: " + e.getMessage()));
        }
    }

    // 个人中心推荐商品
    @GetMapping("/api/recommend/personal")
    public ResponseEntity<?> getRecommendingPersonal(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "16") int size) {
        try {
            Page<Products> result = productService.sFindRecommendingCommodities(page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取商品失败: " + e.getMessage()));
        }
    }

    @GetMapping("/api/search/product")
    public Products getSearchedProduct(@RequestParam int productId) {
        return productService.getSearchedProduct(productId);
    }

    @GetMapping("/api/search/productName")
    public ResponseEntity<?> getSearchedProductName(@RequestParam String keyword,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "16") int size) {
        try {
            Page<Products> result = productService.searchProductsWithPagination(keyword, page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "搜索失败: " + e.getMessage()));
        }
    }

    @GetMapping("/api/search/productCategory")
    public ProductCategory getSearchPrductCategory(@RequestParam int categoryId) {
        return productService.getSearchPrductCategory(categoryId);
    }

    @GetMapping("/api/productCategory")
    public Result<ProductCategory> setPrductCategory(@RequestParam String categoriesName) {
        return productService.setProductCategory(categoriesName);
    }

    @PostMapping("/api/updateProductStock")
    public Result<?> updateProductStock(@RequestParam int productId, @RequestParam int reduceStock) {
        return productService.updateProductStock(productId, reduceStock);
    }

}
