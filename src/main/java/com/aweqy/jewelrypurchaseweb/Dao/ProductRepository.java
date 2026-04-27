package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.ProductCategory;
import com.aweqy.jewelrypurchaseweb.jpw.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products, Integer> {

    @Query(value = """
            SELECT p FROM Products p JOIN p.clickStats s where p.isSell = 1 ORDER BY s.currentScore DESC""")
    Page<Products> rFindRecommendingCommodities(Pageable pageable);

    @Query("select p from Products p where p.id = :productId")
    Products getSearchedProduct(@Param("productId") int productId);

    @Query("select p from ProductCategory p where p.categoryId = :categoryId")
    ProductCategory getSearchPrductCategory(int categoryId);

    @Query("select p from ProductCategory p where p.categoriesName = :categoriesName")
    ProductCategory getProductCategory(String categoriesName);

    @Query(value = "SELECT * FROM products WHERE name LIKE %:keyword%",
            countQuery = "SELECT count(*) FROM products WHERE name LIKE %:keyword%",
            nativeQuery = true)
    Page<Products> searchByNameNative(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p.stock from Products p where p.id = :productId")
    int getProductStock(@Param("productId") int productId);

    @Modifying
    @Query("update Products p set p.stock = :stock , p.version = :version + 1 " +
            "where p.id = :productId and p.version = :version")
    int updateProductStock(int productId, int stock, int version);

    // 自定义查询：按销量降序获取商品
    @Query("SELECT p FROM Products p JOIN p.clickStats s where p.isSell = 1 ORDER BY s.currentScore DESC")
    List<Products> findTopNPopularProducts(Pageable pageable);
}
