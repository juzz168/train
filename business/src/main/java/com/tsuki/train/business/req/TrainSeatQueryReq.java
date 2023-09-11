package com.tsuki.train.business.req;

import com.tsuki.train.common.req.PageReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TrainSeatQueryReq extends PageReq {

    private String trainCode;
}
