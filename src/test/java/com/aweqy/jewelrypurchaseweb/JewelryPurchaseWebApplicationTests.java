package com.aweqy.jewelrypurchaseweb;

import com.aweqy.jewelrypurchaseweb.Dao.OrderRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductClickStatsRepository;
import com.aweqy.jewelrypurchaseweb.Dao.UserSimilarityRepository;
import com.aweqy.jewelrypurchaseweb.service.CollaborativeFilteringService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
class JewelryPurchaseWebApplicationTests {

    @Autowired
    private ProductClickStatsRepository productClickStatsRepository;

    @Autowired
    private CollaborativeFilteringService collaborativeFilteringService;

    @Autowired
    private UserSimilarityRepository userSimilarityRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testConcurrentDeductStock() throws InterruptedException {

        int threadCount = 2000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    productClickStatsRepository.incrementClicksAndCalculateScore(22208623); // 扣减商品ID=1的库存，每次扣1
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        System.out.println("所有线程执行完毕");
    }
}
