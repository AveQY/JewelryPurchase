package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.ProductClickStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductClickStatsRepository extends JpaRepository<ProductClickStats, Long> {

    @Modifying
    @Query(
            value = "INSERT INTO product_click_stats (product_id, total_clicks, current_score)\n" +
                    "VALUES (:productId, 8, 3)" +
                    "ON DUPLICATE KEY UPDATE " +
                    "total_clicks = total_clicks + 1, " +
                    "current_score = CASE " +
                    "    WHEN LOG(2, total_clicks + 1) = FLOOR(LOG(2, total_clicks + 1)) " +
                    "    THEN current_score + 1 " +
                    "    ELSE current_score " +
                    "END",
            nativeQuery = true
    )
    @Transactional
    void incrementClicksAndCalculateScore(@Param("productId") int productId);

}
