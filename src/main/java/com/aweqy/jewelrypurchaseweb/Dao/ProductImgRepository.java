package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT u FROM ProductImage u where u.productId = :id")
    List<ProductImage> searchProductImageById(@Param("id") int id);

}

