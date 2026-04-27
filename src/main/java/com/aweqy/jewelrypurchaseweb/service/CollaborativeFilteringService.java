package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.OrderRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductRepository;
import com.aweqy.jewelrypurchaseweb.Dao.UserSimilarityRepository;
import com.aweqy.jewelrypurchaseweb.jpw.Orders;
import com.aweqy.jewelrypurchaseweb.jpw.Products;
import com.aweqy.jewelrypurchaseweb.jpw.User;
import com.aweqy.jewelrypurchaseweb.jpw.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 个人中心推荐商品
 */
@Service
public class CollaborativeFilteringService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserSimilarityRepository userSimilarityRepository;

    @Autowired
    private ProductRepository productRepository;

    // 为用户生成推荐商品
    public Page<Products> recommendProducts(Integer userId, int page, int limit) {
        // 1. 获取相似用户
        Pageable pageable = PageRequest.of(page, limit);
        List<UserSimilarity> similarUsers = userSimilarityRepository
                .findTop10ByUserUserIdOrderBySimilarityScoreDesc(userId);

        // 退回热门推荐
        if (similarUsers.isEmpty()) {
            return productRepository.rFindRecommendingCommodities(
                    PageRequest.of(page, limit, Sort.by("name").ascending())
            );
        }

        // 2. 获取相似用户购买的商品
        Set<Integer> similarUserIds = similarUsers.stream()
                .map(us -> us.getSimilarUser().getUserId())
                .collect(Collectors.toSet());

        List<Orders> neighborOrders = orderRepository.findOrdersByUserIds(similarUserIds.stream().toList());

        // 3. 获取目标用户已购买的商品
        Set<Integer> userPurchasedProductIds = orderRepository
                .findAllByUserId(userId)
                .stream()
                .map(Orders::getProductId)
                .collect(Collectors.toSet());

        // 4. 计算推荐商品得分
        Map<Integer, Double> productScores = new HashMap<>();

        for (Orders order : neighborOrders) {
            Integer productId = order.getProductId();

            // 排除用户已购买的商品
            if (userPurchasedProductIds.contains(productId)) {
                continue;
            }

            // 计算加权得分
            BigDecimal similarity = similarUsers.stream()
                    .filter(us -> String.valueOf(us.getSimilarUser().getUserId()).equals(String.valueOf(order.getUserId())))
                    .findFirst()
                    .map(UserSimilarity::getSimilarityScore)
                    .orElse(BigDecimal.valueOf(0.0));

            BigDecimal weightedScore = similarity.multiply(BigDecimal.valueOf(order.getPurchaseQuantity()));
            productScores.merge(productId, weightedScore.doubleValue(), Double::sum);
        }

        // 5. 获取商品详情并排序
        List<Integer> productIds = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 退回热门推荐
        if (productIds.isEmpty()) {
            return productRepository.rFindRecommendingCommodities(
                    PageRequest.of(page, limit, Sort.by("name").ascending())
            );
        }

        return findProductsByIds(productIds, pageable);
    }

    public Page<Products> findProductsByIds(List<Integer> productIds, Pageable pageable) {
        // 1. 查询所有匹配的 ID 的记录
        List<Products> allProducts = productRepository.findAllById(productIds);

        // 2. 手动分页逻辑
        int totalElements = allProducts.size();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Products> pageContent;
        if (startItem >= totalElements) {
            pageContent = Collections.emptyList(); // 超出范围返回空列表
        } else {
            int endItem = Math.min(startItem + pageSize, totalElements);
            pageContent = allProducts.subList(startItem, endItem); // 截取当前页数据
        }

        // 3. 构造 Page 对象
        return new PageImpl<>(pageContent, pageable, totalElements);
    }

    // 计算并存储用户相似度
    @Scheduled(fixedRate = 86400000) // 每天计算一次
    @Transactional
    public void computeUserSimilarities() {
        // 获取所有订单
        List<Orders> allOrders = orderRepository.findAll();

        // 按用户分组
        Map<Integer, List<Orders>> ordersByUser = allOrders.stream()
                .collect(Collectors.groupingBy(Orders::getUserId));

        List<Integer> userIds = new ArrayList<>(ordersByUser.keySet());
        userSimilarityRepository.deleteAll();

        // 计算每对用户的相似度
        for (int i = 0; i < userIds.size(); i++) {
            Integer user1 = userIds.get(i);
            Map<Integer, Integer> user1Purchases = ordersByUser.get(user1).stream()
                    .collect(Collectors.toMap(
                            Orders::getProductId,
                            Orders::getPurchaseQuantity,
                            Integer::sum
                    ));

            for (int j = i + 1; j < userIds.size(); j++) {
                Integer user2 = userIds.get(j);
                Map<Integer, Integer> user2Purchases = ordersByUser.get(user2).stream()
                        .collect(Collectors.toMap(
                                Orders::getProductId,
                                Orders::getPurchaseQuantity,
                                Integer::sum
                        ));

                double similarity = cosineSimilarity(user1Purchases, user2Purchases);

                if (similarity > 0.2) { // 只存储显著相似的用户对
                    saveSimilarity(user1, user2, similarity);
                }
            }
        }
    }

    private double cosineSimilarity(Map<Integer, Integer> vectorA,
                                    Map<Integer, Integer> vectorB) {
        Set<Integer> commonProducts = new HashSet<>(vectorA.keySet());
        commonProducts.retainAll(vectorB.keySet());

        if (commonProducts.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        // 计算共同商品的点积和范数
        for (Integer productId : commonProducts) {
            int a = vectorA.get(productId);
            int b = vectorB.get(productId);
            dotProduct += a * b;
            normA += a * a;
            normB += b * b;
        }

        // 计算非共同商品对范数的贡献
        for (Integer productId : vectorA.keySet()) {
            if (!commonProducts.contains(productId)) {
                int a = vectorA.get(productId);
                normA += a * a;
            }
        }

        for (Integer productId : vectorB.keySet()) {
            if (!commonProducts.contains(productId)) {
                int b = vectorB.get(productId);
                normB += b * b;
            }
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);

        return (normA == 0 || normB == 0) ? 0.0 : dotProduct / (normA * normB);
    }

    private void saveSimilarity(Integer userId1, Integer userId2, double similarity) {
        User user1 = new User();
        user1.setUserId(userId1);

        User user2 = new User();
        user2.setUserId(userId2);

        UserSimilarity userSimilarity = new UserSimilarity();
        userSimilarity.setUser(user1);
        userSimilarity.setSimilarUser(user2);
        userSimilarity.setSimilarityScore(BigDecimal.valueOf(similarity));

        userSimilarityRepository.save(userSimilarity);

        // 对称关系
        UserSimilarity reverseSimilarity = new UserSimilarity();
        reverseSimilarity.setUser(user2);
        reverseSimilarity.setSimilarUser(user1);
        reverseSimilarity.setSimilarityScore(BigDecimal.valueOf(similarity));

        userSimilarityRepository.save(reverseSimilarity);
    }

    // 退回策略：推荐热门商品(按销量)
    private List<Products> recommendPopularProducts(int limit) {
        return productRepository.findTopNPopularProducts(Pageable.ofSize(limit));
    }
}
