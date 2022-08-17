package com.qqz.consumer.controller;

import com.qqz.api.IUserService;
import com.qqz.spring.annotation.TinyRemoteReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qqz @Date:2022/8/17
 */
@RestController
public class HelloController {

    @TinyRemoteReference
    private IUserService userService;

    @GetMapping("/test")
    public String test(){
        return userService.saveUser("qqqqz");
    }
}
