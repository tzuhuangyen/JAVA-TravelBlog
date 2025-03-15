package com.blog.travelblogapp.controller;

import com.blog.travelblogapp.service.JwtService;
import com.blog.travelblogapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.blog.travelblogapp.model.User;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;


        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody User user){

            if(userService.isUsernameExist(user.getUsername())){
                return new ResponseEntity<>("Username already exist",HttpStatus.CONFLICT);
            } else {
                User savedUser = userService.saveUser(user);
                savedUser.setPassword(null);
                return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
            }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user){

        // 驗證密碼是否正確的方式！ 正確的話會給你一個Token！
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }
        else
            //401 UNAUTHORIZED
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

    }

}
