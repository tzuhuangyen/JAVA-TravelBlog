package com.blog.travelblogapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("hello")
    public String greeting(){
        return "Hello world!! I'm top of the world!";
    }
}
