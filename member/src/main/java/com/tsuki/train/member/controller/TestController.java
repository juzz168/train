package com.tsuki.train.member.controller;

import com.tsuki.train.common.resp.CommonResp;
import com.tsuki.train.member.req.TestReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! Business!";
    }

    @PostMapping("/test")
    public CommonResp<String> test(@Valid TestReq req){
        return new CommonResp<>();
    }
}
