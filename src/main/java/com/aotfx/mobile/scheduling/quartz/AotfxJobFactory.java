package com.aotfx.mobile.scheduling.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author smilee
 * @version V.1.0
 * @title
 * @Desc
 * @create 2018-05-07 22:39
 * @description Spring框架注入一个Job类实例后，该实例quartz框架是不会使用的，
 * 因为quartz在添加任务的时候会自己新生成一个实例，而不会使用Spring注入的，
 * 所以quartz自己的工厂创建的实例是不会循环注入里面的自动注入注释的对象，
 * 所以需要修改Quartz默认的适配器任务工厂，解决办法就是：该类是继承了任务工厂，
 * 在SchedulerConfig中使用该工厂。该工厂重写了createJobInstance方法，在该方法中会
 * 注入配置了自动注入注解的对象。
 **/
@Component
public class AotfxJobFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}

