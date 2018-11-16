package com.aotfx.mobile.service.quartz;

import com.aotfx.mobile.dao.entity.JobAndTrigger;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface IJobAndTriggerService extends IService<JobAndTrigger> {
	List<JobAndTrigger> getJobAndTriggerDetails(Page<JobAndTrigger> page);
}
