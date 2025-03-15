package com.blog.travelblogapp.service;

import com.blog.travelblogapp.model.User;
import com.blog.travelblogapp.model.UserPrincipal;
import com.blog.travelblogapp.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service //從資料庫獲取資料，檢查使用者的訊息
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if (user ==null){
            System.out.println("Username not found 404");
            throw new UsernameNotFoundException("Username not found");
        }

        // 返回userDetails物件
        return new UserPrincipal(user);
    }
}
