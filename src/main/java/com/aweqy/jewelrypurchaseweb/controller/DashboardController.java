package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.jpw.Dashboard;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public List<Dashboard> getDashboard(@RequestParam String buyer) {
        return dashboardService.getDashboard(buyer);
    }

    @DeleteMapping("/delete/dashboard/{id}")
    public String deleteDashboardGoods(@PathVariable Long id) {
        dashboardService.deleteDashboardGoods(id);
        return "200";
    }

    @PostMapping("/add/dashboard")
    public Result<Dashboard> addDashboard(@RequestParam int productId, @RequestParam int goodsNum,@RequestParam String buyer) {
        return dashboardService.addDashboard(productId,goodsNum,buyer);
    }

}
