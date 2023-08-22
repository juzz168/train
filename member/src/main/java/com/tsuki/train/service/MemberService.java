package com.tsuki.train.service;

import cn.hutool.core.collection.CollUtil;
import com.tsuki.train.domain.Member;
import com.tsuki.train.domain.MemberExample;
import com.tsuki.train.exception.BusinessException;
import com.tsuki.train.exception.BusinessExceptionEnum;
import com.tsuki.train.mapper.MemberMapper;
import com.tsuki.train.req.MemberRegisterReq;
import com.tsuki.train.util.SnowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberService {
    @Resource
    private MemberMapper memberMapper;
    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public long register(MemberRegisterReq req){
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);
        if (CollUtil.isNotEmpty(list)){
            throw  new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
