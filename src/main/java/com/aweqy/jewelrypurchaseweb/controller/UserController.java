package com.aweqy.jewelrypurchaseweb.controller;

import com.aweqy.jewelrypurchaseweb.jpw.Result;
import com.aweqy.jewelrypurchaseweb.jpw.User;
import com.aweqy.jewelrypurchaseweb.service.UserService;
import com.aweqy.jewelrypurchaseweb.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/users")
    public List<User> getAllUsernames() {
        return userService.getAllUsernames();
    }

    @GetMapping("/login")
    public Result<User> login(@RequestParam String username,
                        @RequestParam String phone,
                        @RequestParam String password) {
        User isLoginUser = userService.isLoginUser(username, phone, password);
        if (isLoginUser != null) {
            return new Result<>("200", getToken(username), isLoginUser);
        }
        return new Result<>("502","账号密码错误",null); // 账号密码错误
    }

    @GetMapping("/register")
    public Result<User> registerUser(@RequestParam String username,
                               @RequestParam String phone,
                               @RequestParam String password) {
        User isRegisterUser = null;
        try {
            isRegisterUser = userService.isRegisterUser(username, phone, password);
        } catch (Exception e) {
            return new Result<>("501","用户名重复，注册失败"+e.getMessage(),null); // 用户名重复，注册失败
        }
        if (isRegisterUser != null) {
            return new Result<>("201", getToken(username), isRegisterUser);
        }
        return new Result<>("505","注册失败",null); // 注册失败
    }

    @GetMapping("/search_user")
    public User registerUser(@RequestParam String username) {
        User isUser = userService.searchUserByUsername(username);
        if (isUser != null) {
            return isUser;
        }
        return null;
    }

    @GetMapping("/detoken")
    public Result<User> detoken(@RequestParam String username) {
        return new Result<>("201", deToken(username),null);
    }

    public String getToken(String username) {
        // 生成 Token
        String token = jwtUtil.generateToken(username);
        return token;
    }

    public String deToken(String oldToken) {
        // 解析 Token
        String token = String.valueOf(jwtUtil.extractAllClaims(oldToken));
        return token;
    }
}
