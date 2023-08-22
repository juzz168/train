package com.tsuki.train.service;

import com.tsuki.train.mapper.MemberMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;
    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }
}
