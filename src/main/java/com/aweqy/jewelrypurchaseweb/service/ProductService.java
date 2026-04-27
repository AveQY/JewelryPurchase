package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.InventoryRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductCategoryRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductClickStatsRepository;
import com.aweqy.jewelrypurchaseweb.Dao.ProductRepository;
import com.aweqy.jewelrypurchaseweb.jpw.ProductCategory;
import com.aweqy.jewelrypurchaseweb.jpw.Products;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductClickStatsRepository productClickStatsRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Page<Products> sFindRecommendingCommodities(int page, int size) {
        return productRepository.rFindRecommendingCommodities(
                PageRequest.of(page, size, Sort.by("name").ascending())
        );
    }

    public Products getSearchedProduct(int productId) {
        Products products = productRepository.getSearchedProduct(productId);
        if(products == null) {
            return new Products(1,
                    "错误",
                    "加载错误！",
                    new BigDecimal(0.00),
                    1,
                    12,"","",0,0);
        }
        try {
            productClickStatsRepository.incrementClicksAndCalculateScore(productId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public ProductCategory getSearchPrductCategory(int categoryId) {
        return productRepository.getSearchPrductCategory(categoryId);
    }

    @Transactional
    public Result<ProductCategory> setProductCategory(String categoriesName) {
        ProductCategory productCategory = productRepository.getProductCategory(categoriesName);
        if (productCategory == null) {
            // 新分类（创建）
            try {
                productCategoryRepository.save(new ProductCategory(categoriesName));
            } catch (Exception e) {
                return new Result<>("404", "数据库执行错误！",null);
            }
            ProductCategory productCategory2 = productRepository.getProductCategory(categoriesName);
            if(productCategory2 != null) {
                return new Result<>("201", String.valueOf(productCategory2.getCategoryId()),productCategory2);
            }
        }
        // 已有分类
        return new Result<>("200", String.valueOf(productCategory.getCategoryId()),productCategory);
    }

    public Page<Products> searchProductsWithPagination(String keyword, int page, int size) {
        return productRepository.searchByNameNative(
                keyword,
                PageRequest.of(page, size, Sort.by("name").ascending())
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateProductStock(int productId, int reduceStock) {
        int maxRetries = 3;
        for (int retry = 0; retry < maxRetries; retry++) {
            Products product = productRepository.getById(productId);

            // 1. 查询当前库存
            int stock = productRepository.getProductStock(productId) - reduceStock;
            if (stock < 0) {
                return new Result<>("402", "库存不足", null);
            }

            // 2. 尝试更新库存（带版本校验）
            int rows = productRepository.updateProductStock(productId, stock, product.getVersion());

            // 3. 更新成功
            if (rows >= 1) {
                if (stock == 0) {
                    inventoryRepository.setIsSell(productId, 0);
                }
                return new Result<>("200", "支付成功！", null);
            }

            // 4. 更新失败，稍后重试（可增加随机延迟避免活锁）
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
//        return new Result<>("404", "当前人数过多，支付失败，请重试！", null);
        throw new RuntimeException("并发冲突，请稍后重试");
    }
}
