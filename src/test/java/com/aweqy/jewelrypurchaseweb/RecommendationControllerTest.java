package com.aweqy.jewelrypurchaseweb;

import com.aweqy.jewelrypurchaseweb.service.CollaborativeFilteringService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RecommendationControllerTest {

    @Autowired
    private CollaborativeFilteringService recommendationService;

    @Test
    public void testConcurrentDeductStock() throws InterruptedException {

        recommendationService.recommendProducts(37, 0, 8);
        System.out.println("执行完毕");
    }
}
