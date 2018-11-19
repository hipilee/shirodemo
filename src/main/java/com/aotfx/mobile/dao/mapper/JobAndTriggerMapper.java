package com.aotfx.mobile.dao.mapper;

import com.aotfx.mobile.dao.entity.JobAndTriggerVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface JobAndTriggerMapper extends BaseMapper<JobAndTriggerVo> {
	List<JobAndTriggerVo> getJobAndTriggerDetails(Page<JobAndTriggerVo> page);

}
