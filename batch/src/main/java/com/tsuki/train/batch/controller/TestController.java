package com.tsuki.train.batch.controller;

import com.tsuki.train.batch.feign.BusinessFeign;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Resource
    BusinessFeign businessFeign;

    @GetMapping("/hello")
    public String hello(){
        String hello = businessFeign.hello();
        log.info(hello);
        return "hello this is batch";
    }
}
