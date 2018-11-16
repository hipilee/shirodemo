package com.aotfx.mobile.service.quartz;

import java.util.Date;

import com.aotfx.mobile.dao.entity.Mt4User;
import com.aotfx.mobile.dao.mapper.Mt4UserMapper;
import com.aotfx.mobile.service.quartz.impl.BaseJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class NewJob implements BaseJob {

    @Autowired
    Mt4UserMapper mt4UserMapper;

    private static Logger _log = LoggerFactory.getLogger(NewJob.class);

    public NewJob() {

    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        _log.error("New Job执行时间: " + new Date());


    }
}
