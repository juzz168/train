package com.tsuki.train.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.tsuki.train.domain.Passenger;
import com.tsuki.train.mapper.PassengerMapper;
import com.tsuki.train.req.PassengerSaveReq;
import com.tsuki.train.util.SnowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req){
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}
