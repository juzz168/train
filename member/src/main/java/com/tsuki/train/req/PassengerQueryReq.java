package com.tsuki.train.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PassengerQueryReq extends PageReq{

    private Long memberId;
}
