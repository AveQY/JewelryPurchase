package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.ProductCategoryRepository;
import com.aweqy.jewelrypurchaseweb.jpw.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> getAllProductCategory() {
        return productCategoryRepository.findAll();
    }

    public Page<ProductCategory> getProductCategoryByCategoryId(int categoryId, int page, int size) {
        return productCategoryRepository.getProductCategoryByCategoryId(
                categoryId,
                PageRequest.of(page, size, Sort.by("name").ascending())
        );
    }
}
