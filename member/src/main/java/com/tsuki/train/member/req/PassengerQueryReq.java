package com.tsuki.train.member.req;

import com.tsuki.train.common.req.PageReq;
import lombok.Data;

@Data
public class PassengerQueryReq extends PageReq {

    private Long memberId;
}
