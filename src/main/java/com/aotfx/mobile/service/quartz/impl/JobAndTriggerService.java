package com.aotfx.mobile.service.quartz.impl;


import com.aotfx.mobile.dao.entity.JobAndTriggerVo;
import com.aotfx.mobile.dao.mapper.JobAndTriggerMapper;
import com.aotfx.mobile.service.quartz.IJobAndTriggerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobAndTriggerService extends ServiceImpl<JobAndTriggerMapper, JobAndTriggerVo> implements IJobAndTriggerService {

    public List<JobAndTriggerVo> getJobAndTriggerDetails(Page<JobAndTriggerVo> page) {
        List<JobAndTriggerVo> list = this.baseMapper.getJobAndTriggerDetails(page);
        return list;
    }

}