package com.tsuki.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tsuki.train.business.domain.Station;
import com.tsuki.train.business.domain.StationExample;
import com.tsuki.train.business.enums.SeatColEnum;
import com.tsuki.train.common.exception.BusinessException;
import com.tsuki.train.common.exception.BusinessExceptionEnum;
import com.tsuki.train.common.resp.PageResp;
import com.tsuki.train.common.util.SnowUtil;
import com.tsuki.train.business.domain.DailyTrainCarriage;
import com.tsuki.train.business.domain.DailyTrainCarriageExample;
import com.tsuki.train.business.mapper.DailyTrainCarriageMapper;
import com.tsuki.train.business.req.DailyTrainCarriageQueryReq;
import com.tsuki.train.business.req.DailyTrainCarriageSaveReq;
import com.tsuki.train.business.resp.DailyTrainCarriageQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DailyTrainCarriageService {

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    public void save(DailyTrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {

            //保存之前，先校验唯一键
            DailyTrainCarriage stationDB = selectByUnique(req.getDate(), req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotEmpty(stationDB)){
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }

            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateByPrimaryKey(dailyTrainCarriage);
        }
    }

    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, 'index' asc");
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();

        if (ObjectUtil.isNotNull(req.getDate())){
            criteria.andDateEqualTo(req.getDate());
        }

        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }
        log.info("查询页码：{}", req.getPage());
        log.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainCarriageQueryResp> list = BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageQueryResp.class);

        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    private DailyTrainCarriage selectByUnique(Date date, String trainCode, Integer index) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        criteria.andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode)
                .andIndexEqualTo(index);
        List<DailyTrainCarriage> list = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);
        if (CollUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }
}
