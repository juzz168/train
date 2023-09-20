package com.tsuki.train.member.controller.admin;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.tsuki.train.common.context.LoginMemberContext;
import com.tsuki.train.common.resp.CommonResp;
import com.tsuki.train.common.resp.PageResp;
import com.tsuki.train.member.req.SysUserLoginReq;
import com.tsuki.train.member.req.SysUserQueryReq;
import com.tsuki.train.member.req.SysUserSaveReq;
import com.tsuki.train.member.resp.SysUserLoginResp;
import com.tsuki.train.member.resp.SysUserQueryResp;
import com.tsuki.train.member.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin/sys-user")
public class SysUserAdminController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
          sysUserService.verify(request, response);
    }


    @PostMapping("/login")
    public CommonResp<Object> login(@Valid @RequestBody SysUserLoginReq req) {
        SysUserLoginResp resp = sysUserService.login(req);
        return new CommonResp<>(resp);
    }

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody SysUserSaveReq req) {
        sysUserService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<SysUserQueryResp>> queryList(@Valid SysUserQueryReq req) {
        PageResp<SysUserQueryResp> list = sysUserService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        sysUserService.delete(id);
        return new CommonResp<>();
    }

}
