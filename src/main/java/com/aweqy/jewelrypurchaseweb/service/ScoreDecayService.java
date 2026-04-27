package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.ProductClickStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权重评分自动衰减
 */

@Service
public class ScoreDecayService {
    @Autowired
    ProductClickStatsRepository productClickStatsRepository;

    @Scheduled(cron = "0 0 3 ? * MON") // 每周一凌晨3点执行
    @Transactional
    public void applyDailyDecay() {
        productClickStatsRepository.findAll().forEach(stat -> {
            int newScore = Math.max(stat.getCurrentScore() - 1, 0);
            stat.setCurrentScore(newScore);
            productClickStatsRepository.save(stat);
        });
    }
}
