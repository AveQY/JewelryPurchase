package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.DashboardRepository;
import com.aweqy.jewelrypurchaseweb.jpw.Dashboard;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    public List<Dashboard> getDashboard(String buyer) {
        return dashboardRepository.getDashboard(buyer);
    }

    public void deleteDashboardGoods(Long id) {
        dashboardRepository.deleteById(id);
    }

    public Result<Dashboard> addDashboard(int productId, int goodsNum, String buyer) {
        // 检查之前购物车是否已存在该商品
        Optional<Dashboard> existingDashboard = dashboardRepository.findByProductId(productId, buyer);
        if (existingDashboard.isPresent()) {
            System.out.println("已存在");
            return new Result<>("200","未执行，存在相同数据！",null);
        }
        try {
            dashboardRepository.save(new Dashboard(productId, goodsNum, buyer));
        } catch (Exception e) {
            return new Result<>("404","执行错误！",null);
        }
        return new Result<>("200","执行成功！",null);
    }
}
