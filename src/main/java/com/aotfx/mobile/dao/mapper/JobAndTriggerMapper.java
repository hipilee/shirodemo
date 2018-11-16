package com.aotfx.mobile.dao.mapper;

import com.aotfx.mobile.dao.entity.JobAndTrigger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface JobAndTriggerMapper extends BaseMapper<JobAndTrigger> {
	List<JobAndTrigger> getJobAndTriggerDetails(Page<JobAndTrigger> page);

}
