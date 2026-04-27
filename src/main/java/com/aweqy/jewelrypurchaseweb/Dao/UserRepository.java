package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u")
    List<User> findAllUsername();

    @Query("SELECT u FROM User u WHERE u.username = :name AND u.password = :pd and u.phone = :phone")
    User verifyLogin(@Param("name") String username,@Param("phone") String phone, @Param("pd") String password);

    // 查找用户名（防止重复）
    Optional<User> findByUsername(String username);

}