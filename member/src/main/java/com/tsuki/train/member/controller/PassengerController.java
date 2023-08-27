package com.tsuki.train.member.controller;

import com.tsuki.train.common.context.LoginMemberContext;
import com.tsuki.train.member.service.PassengerService;
import com.tsuki.train.member.req.PassengerQueryReq;
import com.tsuki.train.member.req.PassengerSaveReq;
import com.tsuki.train.common.resp.CommonResp;
import com.tsuki.train.common.resp.PageResp;
import com.tsuki.train.member.resp.PassengerQueryResp;
import com.tsuki.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req){
        passengerService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req){
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> list = passengerService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id){
        passengerService.delete(id);
        return new CommonResp<>();
    }
}
