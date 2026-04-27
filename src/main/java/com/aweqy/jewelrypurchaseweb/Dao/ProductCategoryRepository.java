package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("SELECT p FROM Products p WHERE p.categoryId = :categoryId and p.isSell = 1")
    Page<ProductCategory> getProductCategoryByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);

}
