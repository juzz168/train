package com.tsuki.train.business.req;

import com.tsuki.train.common.req.PageReq;
import lombok.Data;

@Data
public class TrainCarriageQueryReq extends PageReq {

    private String trainCode;

}
