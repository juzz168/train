package com.tsuki.train.member.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tsuki.train.common.exception.SysUserException;
import com.tsuki.train.common.exception.SysUserExceptionEnum;
import com.tsuki.train.common.resp.PageResp;
import com.tsuki.train.common.util.JwtUtil;
import com.tsuki.train.common.util.SnowUtil;
import com.tsuki.train.member.domain.SysUser;
import com.tsuki.train.member.domain.SysUserExample;
import com.tsuki.train.member.mapper.SysUserMapper;
import com.tsuki.train.member.req.SysUserLoginReq;
import com.tsuki.train.member.req.SysUserQueryReq;
import com.tsuki.train.member.req.SysUserSaveReq;
import com.tsuki.train.member.resp.SysUserLoginResp;
import com.tsuki.train.member.resp.SysUserQueryResp;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    public SysUserLoginResp login(SysUserLoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();
        String code = req.getCode();
        SysUser sysUser = selectByUsername(username);
        //如果用户名不存在，则插入一条记录
        if (ObjectUtil.isNull(sysUser)) {
            throw new SysUserException(SysUserExceptionEnum.SYS_USER_NOT_EXIST);
        }
        if (!sysUser.getPassword().equals(code)) {
            throw new SysUserException(SysUserExceptionEnum.SYS_USER_PASSWORD_ERROR);
        }
        if (!sysUser.getPassword().equals(password)) {
            throw new SysUserException(SysUserExceptionEnum.SYS_USER_PASSWORD_ERROR);
        }
        SysUserLoginResp sysUserLoginResp = BeanUtil.copyProperties(sysUser, SysUserLoginResp.class);
        String token = JwtUtil.createToken(sysUserLoginResp.getId(), sysUserLoginResp.getMobile());
        sysUserLoginResp.setToken(token);
        return sysUserLoginResp;
    }


    private SysUser selectByUsername(String username) {
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.createCriteria().andUsernameEqualTo(username);
        List<SysUser> list = sysUserMapper.selectByExample(sysUserExample);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public void save(SysUserSaveReq req) {
        DateTime now = DateTime.now();
        SysUser sysUser = BeanUtil.copyProperties(req, SysUser.class);
        if (ObjectUtil.isNull(sysUser.getId())) {
            sysUser.setId(SnowUtil.getSnowflakeNextId());
            sysUser.setCreateTime(now);
            sysUserMapper.insert(sysUser);
        } else {
            sysUserMapper.updateByPrimaryKey(sysUser);
        }
    }

    public PageResp<SysUserQueryResp> queryList(SysUserQueryReq req) {
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.setOrderByClause("id desc");
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();

        log.info("查询页码：{}", req.getPage());
        log.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SysUser> sysUserList = sysUserMapper.selectByExample(sysUserExample);

        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUserList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<SysUserQueryResp> list = BeanUtil.copyToList(sysUserList, SysUserQueryResp.class);

        PageResp<SysUserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        sysUserMapper.deleteByPrimaryKey(id);
    }

    public void verify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
        //图形验证码写出，可以写出到文件，也可以写出到流
        shearCaptcha.write(response.getOutputStream());
        //获取验证码中的文字内容
       // request.getSession().setAttribute("verifyCode", shearCaptcha.getCode());
    }
}
