package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.Util.JwtUtil;
import com.aweqy.jewelrypurchaseweb.jpw.Inventory;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.service.InventoryService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/inventory")
    public Result<?> searchInventory(@RequestParam String author) {

        String s1 = deToken(author);
        String fixedJson = s1
                .replaceAll("([a-zA-Z]+)=", "\"$1\":")  // 将 "key=" 替换为 "key":
                .replaceAll("([^\"\\d])([^,:{}]+)(,|})", "$1\"$2\"$3");

//        System.out.println(fixedJson);

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(fixedJson);
        } catch (JSONException e) {
            System.err.println("JSON 解析失败：" + e.getMessage());
            return null;
        }
        String name = jsonObj.getString("sub");

        return inventoryService.searchInventoryByAuthor(name);
    }

    @PostMapping("/addInventory")
    public Result<Inventory> addInventory(@RequestParam int productId,
                                          @RequestParam String name,
                                          @RequestParam String description,
                                          @RequestParam String price,
                                          @RequestParam int categoryId,
                                          @RequestParam int stock,
                                          @RequestParam String imageUrl,
                                          @RequestParam String author,
                                          @RequestParam String isSell) {
        Inventory inventory = new Inventory();
        inventory.setId(productId);
        inventory.setName(name);
        inventory.setDescription(description);
        inventory.setPrice(price);
        inventory.setCategoryId(categoryId);
        inventory.setStock(stock);
        inventory.setImageUrl(imageUrl);
        inventory.setAuthor(author);
        inventory.setIsSell(isSell);

        return inventoryService.addInventory(inventory);
    }

    @DeleteMapping("/delete/inventory/{id}")
    public Result deleteInventory(@PathVariable Long id, @RequestParam String author) {

        JSONObject jsonObj = null;

        try {
            String s1 = deToken(author);
            String fixedJson = s1
                    .replaceAll("([a-zA-Z]+)=", "\"$1\":")  // 将 "key=" 替换为 "key":
                    .replaceAll("([^\"\\d])([^,:{}]+)(,|})", "$1\"$2\"$3");

            jsonObj = new JSONObject(fixedJson);
        } catch (Exception e) {
            return new Result("402", "令牌错误", null);
        }
        String name = jsonObj.getString("sub");
        System.out.println(id);
        return inventoryService.deleteInventory(id, name);
    }

    @GetMapping("/isSell")
    public Result<?> isSell(@RequestParam int id, @RequestParam int isSell) {
        return inventoryService.setIsSell(id, isSell);
    }

    public String deToken(String oldToken) {
        // 解析 Token
        String token = String.valueOf(jwtUtil.extractAllClaims(oldToken));
        return token;
    }
}
