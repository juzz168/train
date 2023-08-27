package com.tsuki.train.member.req;

import com.tsuki.train.common.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberSendCodeReq {

    @NotBlank(message = "【手机号】不能为空")
    @Pattern(regexp = RegexPatterns.PHONE_REGEX, message = "手机号码格式错误")
    private String mobile;
}
