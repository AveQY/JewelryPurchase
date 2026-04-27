package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.jpw.Products;
import com.aweqy.jewelrypurchaseweb.service.CollaborativeFilteringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {

    @Autowired
    private CollaborativeFilteringService recommendationService;

    @GetMapping("/personal/{userId}")
    public ResponseEntity<?> getRecommendations(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int limit) {

        Page<Products> recommendations = recommendationService.recommendProducts(userId, page, limit);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/compute-similarities")
    public ResponseEntity<String> computeSimilarities() {
        recommendationService.computeUserSimilarities();
        return ResponseEntity.ok("User similarity computation completed");
    }
}
