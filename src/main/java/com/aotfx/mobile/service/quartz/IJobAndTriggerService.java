package com.aotfx.mobile.service.quartz;

import com.aotfx.mobile.dao.entity.JobAndTriggerVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IJobAndTriggerService extends IService<JobAndTriggerVo> {
	List<JobAndTriggerVo> getJobAndTriggerDetails(Page<JobAndTriggerVo> page);
}
