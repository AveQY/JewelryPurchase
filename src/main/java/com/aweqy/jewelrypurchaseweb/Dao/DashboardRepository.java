package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    @Query("select d from Dashboard d where d.buyer = :buyer")
    List<Dashboard> getDashboard(@Param("buyer") String buyer);

    @Query("select d from Dashboard d where d.buyer = :buyer and d.productId = :productId")
    Optional<Dashboard> findByProductId(int productId, String buyer);

    List<Dashboard> findByProductId(Long productId);
}
