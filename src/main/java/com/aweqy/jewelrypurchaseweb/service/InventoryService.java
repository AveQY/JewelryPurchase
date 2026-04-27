package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.DashboardRepository;
import com.aweqy.jewelrypurchaseweb.Dao.InventoryRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductClickStatsRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductRepository;
import com.aweqy.jewelrypurchaseweb.jpw.Dashboard;
import com.aweqy.jewelrypurchaseweb.jpw.Inventory;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private ProductClickStatsRepository productClickStatsRepository;

    public Result<?> searchInventoryByAuthor(String author) {
        System.out.println(author);
        List<Inventory> existingfindByAuthor = inventoryRepository.findByAuthor(author);
        if (!existingfindByAuthor.isEmpty()) {
            return new Result<>("200", "success", existingfindByAuthor);
        }
        return new Result<>("404", "未查到数据", null);
    }

    @Transactional
    public Result<Inventory> addInventory(Inventory inventory) {
        try {
            inventoryRepository.save(inventory);
            inventoryRepository.flush();
        } catch (Exception e) {
            return new Result<>("404", "fail", null);
        }
        productClickStatsRepository.incrementClicksAndCalculateScore((int) inventory.getId());
        return new Result<>("200", "success", inventory);
    }

    public Result<?> setIsSell(int productId, int isSell) {
        int stock = productRepository.getProductStock(productId);
        if (stock > 0) {
            int products = inventoryRepository.setIsSell(productId, isSell);
            if (products >= 1) {
                return new Result<>("200", "success", products);
            }
        }
        return new Result<>("404", "fail", null);
    }

    @Transactional
    public Result<?> deleteInventory(Long id, String author) {
        List<Inventory> existingfindByAuthor = inventoryRepository.findByAuthor(author);
        if (!existingfindByAuthor.isEmpty()) {
            // 检查是否存在关联的仪表盘记录
            List<Dashboard> dashboards = dashboardRepository.findByProductId(id);
            if (!dashboards.isEmpty()) {
                // 可选方案2：自动删除关联记录（需业务确认）
                dashboardRepository.deleteAll(dashboards);
                // 可选方案1：阻止删除并返回错误
//                return new Result<>("403", "删除失败：存在关联的仪表盘记录，请先处理", null);

            }
            try {
                inventoryRepository.deleteById(id);
                return new Result<>("200", author + "：删除成功：" + id, null);
            } catch (Exception e) {
                // 捕获其他可能的异常
                return new Result<>("500", "删除失败：" + e.getMessage(), null);
            }
        }
        return new Result<>("404", author + "：没有权限执行该操作", null);
    }
}
