package com.tsuki.train.member.req;

import jakarta.validation.constraints.NotBlank;

public class TestReq {

    @NotBlank(message = "姓名不能空~~~~")
    private String name;
}
