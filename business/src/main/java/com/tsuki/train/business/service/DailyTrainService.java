package com.tsuki.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tsuki.train.business.domain.Train;
import com.tsuki.train.common.resp.PageResp;
import com.tsuki.train.common.util.SnowUtil;
import com.tsuki.train.business.domain.DailyTrain;
import com.tsuki.train.business.domain.DailyTrainExample;
import com.tsuki.train.business.mapper.DailyTrainMapper;
import com.tsuki.train.business.req.DailyTrainQueryReq;
import com.tsuki.train.business.req.DailyTrainSaveReq;
import com.tsuki.train.business.resp.DailyTrainQueryResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DailyTrainService {

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainService trainService;

    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();

        if (ObjectUtil.isNotNull(req.getDate())){
            criteria.andDateEqualTo(req.getDate());
        }

        if (StrUtil.isNotBlank(req.getCode())){
            criteria.andCodeEqualTo(req.getCode());
        }

        log.info("查询页码：{}", req.getPage());
        log.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class);

        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /*
    * 生成某日车次所有信息，包括车次，车站，车厢，座位
    * */
    public void getDaily(Date date){
        List<Train> trainList = trainService.selectAll();
        if (CollUtil.isEmpty(trainList)){
            log.info("没有车次基础次序");
            return;
        }
        for (Train item : trainList) {
            //删除该车次已有的数据
            DailyTrainExample dailyTrainExample = new DailyTrainExample();
            dailyTrainExample.createCriteria().andDateEqualTo(date).andCodeEqualTo(item.getCode());
            dailyTrainMapper.deleteByExample(dailyTrainExample);

            //生成该车次的数据
            DateTime now = DateTime.now();
            DailyTrain dailyTrain = BeanUtil.copyProperties(item, DailyTrain.class);
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrain.setDate(date);
            dailyTrainMapper.insert(dailyTrain);
        }
    }
    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }
}
