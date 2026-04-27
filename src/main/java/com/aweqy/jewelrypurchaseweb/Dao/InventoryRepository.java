package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 查找库存所有者
    @Query("select p from Inventory p where p.author = :author")
    List<Inventory> findByAuthor(String author);

    @Modifying
    @Transactional
    @Query("update Products p set p.isSell = :isSell where p.id = :productId")
    int setIsSell(@Param("productId") int productId, @Param("isSell") int isSell);

    @Modifying
    @Query("""
        UPDATE Products p
        SET
            p.name = CASE WHEN :name IS NOT NULL THEN :name ELSE p.name END,
            p.description = CASE WHEN :description IS NOT NULL THEN :description ELSE p.description END,
            p.price = CASE WHEN :price IS NOT NULL THEN :price ELSE p.price END,
            p.categoryId = CASE WHEN :categoryId IS NOT NULL THEN :categoryId ELSE p.categoryId END,
            p.stock = CASE WHEN :stock IS NOT NULL THEN :stock ELSE p.stock END,
            p.imageUrl = CASE WHEN :imageUrl IS NOT NULL THEN :imageUrl ELSE p.imageUrl END,
            p.author = CASE WHEN :author IS NOT NULL THEN :author ELSE p.author END,
            p.isSell = CASE WHEN :isSell IS NOT NULL THEN :isSell ELSE p.isSell END
        WHERE p.id = :productId
        """)
    int updateRepository(@Param("productId") int productId,
                          @Param("name") String name,
                          @Param("description") String description,
                          @Param("price") String price,
                          @Param("categoryId") int categoryId,
                          @Param("stock") Integer stock,
                          @Param("imageUrl") String imageUrl,
                          @Param("author") String author,
                          @Param("isSell") String isSell);
}
