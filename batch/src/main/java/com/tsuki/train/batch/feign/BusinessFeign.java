package com.tsuki.train.batch.feign;

import com.tsuki.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@FeignClient(name = "business", url = "http://127.0.0.1:8002/business")
public interface BusinessFeign {
    @GetMapping("/hello")
    public String hello();

    @GetMapping("/admin/daily-train/get-daily/{date}")
    CommonResp<Object> getDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
