package com.tsuki.train.common.exception;

public enum SysUserExceptionEnum {
    SYS_USER_NOT_EXIST("用户名不存在"),
    SYS_USER_PASSWORD_ERROR("密码错误"),
    SYS_USER_CODE_NOT_EXIST("请先获取短信验证码"),
    SYS_USER_CODE_ERROR("验证码错误");


    private String desc;

    SysUserExceptionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SysUserExceptionEnum{" +
                "desc='" + desc + '\'' +
                "} " + super.toString();
    }
}
