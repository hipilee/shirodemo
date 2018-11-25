package com.aotfx.mobile.service.quartz.job;

import java.util.Date;

import com.aotfx.mobile.dao.mapper.Mt4AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class NewJob implements BaseJob {

    @Autowired
    Mt4AccountMapper mt4UserMapper;

    private static Logger _log = LoggerFactory.getLogger(NewJob.class);

    public NewJob() {

    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        _log.error("New Job执行时间: " + new Date());


    }
}
