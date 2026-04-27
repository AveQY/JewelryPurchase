package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("""
            select o from Orders o where o.userId = :userId""")
    List<Orders> findAllByUserId(int userId);


    @Query("SELECT o FROM Orders o WHERE o.userId IN :userIds")
    List<Orders> findOrdersByUserIds(@Param("userIds") List<Integer> userIds);
}
