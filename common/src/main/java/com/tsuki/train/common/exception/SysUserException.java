package com.tsuki.train.common.exception;

public class SysUserException extends RuntimeException{

    private SysUserExceptionEnum e;

    public SysUserException(SysUserExceptionEnum e){
        this.e = e;
    }

    public SysUserExceptionEnum getE() {
        return e;
    }

    public void setE(SysUserExceptionEnum e) {
        this.e = e;
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
