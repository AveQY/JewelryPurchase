package com.aweqy.jewelrypurchaseweb.Dao;

import com.aweqy.jewelrypurchaseweb.jpw.UserSimilarity;
import com.aweqy.jewelrypurchaseweb.jpw.UserSimilarityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSimilarityRepository extends JpaRepository<UserSimilarity, UserSimilarityId> {

    // 查找与指定用户最相似的用户
    @Query("SELECT us FROM UserSimilarity us WHERE us.user.userId = :userId ORDER BY us.similarityScore DESC")
    List<UserSimilarity> findTop10ByUserUserIdOrderBySimilarityScoreDesc(@Param("userId") Integer userId);

    // 清空所有相似度记录
    void deleteAll();
}
