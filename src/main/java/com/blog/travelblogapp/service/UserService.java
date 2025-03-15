package com.blog.travelblogapp.service;

import com.blog.travelblogapp.model.User;
import com.blog.travelblogapp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public boolean isUsernameExist(String user) {
        return userRepo.existsByUsername(user);
    }

    // 加密密碼，再存到資料庫
    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.getPassword());

        return userRepo.save(user);
    }


}
