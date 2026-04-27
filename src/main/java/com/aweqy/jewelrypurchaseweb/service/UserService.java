package com.aweqy.jewelrypurchaseweb.service;

import com.aweqy.jewelrypurchaseweb.Dao.UserRepository;
import com.aweqy.jewelrypurchaseweb.jpw.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<User> getAllUsernames() {
        return userRepository.findAllUsername();
    }

    public User isLoginUser(String username,String phone, String password) {
        return userRepository.verifyLogin(username,phone,password);
    }

    public User isRegisterUser(String username, String phone, String password) throws Exception {
        // 检查用户名是否已存在
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new Exception("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);

        return userRepository.save(user);
    }

    public User searchUserByUsername(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        return null;
    }
}
