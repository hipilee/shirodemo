package com.aotfx.mobile.service.quartz.impl;


import com.aotfx.mobile.dao.mapper.JobAndTriggerMapper;
import com.aotfx.mobile.dao.entity.JobAndTrigger;
import com.aotfx.mobile.service.quartz.IJobAndTriggerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobAndTriggerService extends ServiceImpl<JobAndTriggerMapper, JobAndTrigger> implements IJobAndTriggerService {

    public List<JobAndTrigger> getJobAndTriggerDetails(Page<JobAndTrigger> page) {
        List<JobAndTrigger> list = this.baseMapper.getJobAndTriggerDetails(page);
        return list;
    }

}