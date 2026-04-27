package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.Util.JwtUtil;
import com.aweqy.jewelrypurchaseweb.jpw.Address;
import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.service.AddressService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/get/address")
    public Result<?> getAddress(@RequestParam int addressId) {
        return addressService.getAddress(addressId);
    }

    @GetMapping("/search/address")
    public List<Address> searchAddress(@RequestParam String userToken) {
        JSONObject jsonObj = null;
        try {
            String s1 = deToken(userToken);
            String fixedJson = s1
                    .replaceAll("([a-zA-Z]+)=", "\"$1\":")  // 将 "key=" 替换为 "key":
                    .replaceAll("([^\"\\d])([^,:{}]+)(,|})", "$1\"$2\"$3");

            jsonObj = new JSONObject(fixedJson);
        } catch (Exception e) {
            return null;
        }
        String name = jsonObj.getString("sub");
        return addressService.searchAddress(name);
    }

    @PostMapping("/add/address")
    public Result<Address> addAddress(@RequestParam String userId,
                                      @RequestParam String addressName,
                                      @RequestParam String phone,
                                      @RequestParam String username) {
        JSONObject jsonObj = null;
        try {
            String s1 = deToken(userId);
            String fixedJson = s1
                    .replaceAll("([a-zA-Z]+)=", "\"$1\":")  // 将 "key=" 替换为 "key":
                    .replaceAll("([^\"\\d])([^,:{}]+)(,|})", "$1\"$2\"$3");

            jsonObj = new JSONObject(fixedJson);
        } catch (Exception e) {
            return null;
        }
        String name = jsonObj.getString("sub");
        return addressService.addAddress(name,addressName,phone,username);
    }

    @DeleteMapping("/delete/address/{addressId}")
    public Result<Address> deleteAddress(@PathVariable int addressId) {
        return addressService.deleteAddress(addressId);
    }

    public String deToken(String oldToken) {
        // 解析 Token
        String token = String.valueOf(jwtUtil.extractAllClaims(oldToken));
        return token;
    }
}
