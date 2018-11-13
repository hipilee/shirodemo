package com.aotfx.mobile.dao;



import com.aotfx.mobile.dao.entity.JobAndTrigger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAndTriggerMapper {
	List<JobAndTrigger> getJobAndTriggerDetails();
}
