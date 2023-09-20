package com.tsuki.train.member.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SysUserLoginReq {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "用户名不能为空")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "【验证码】不能为空")
    private String code;

}
