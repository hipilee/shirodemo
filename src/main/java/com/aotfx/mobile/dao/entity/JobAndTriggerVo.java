package com.aotfx.mobile.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobAndTriggerVo {
    //变量名的命名一定要严格遵守驼峰规则，切记！
    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private String triggerName;
    private String TriggerGroup;
    private BigInteger repeatInterval;
	private BigInteger timesTriggered;
    private String cronExpression;
    private String timeZoneId;
}
