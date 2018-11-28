package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.IMT4AccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jfx.Broker;
import com.jfx.strategy.NJ4XMaxNumberOfTerminalsExceededException;
import com.jfx.strategy.Strategy;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 更新MT4服务器时间和GMT时间之间的时差，该任务最好不要在周六周天运行，因为休市后是获取不到MT4服务器信息的。
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-25 20:36
 */
@DisallowConcurrentExecution
public class SynTimeZoneOffset implements BaseJob {

    private static Logger log = LoggerFactory.getLogger(SynTimeZoneOffset.class);

    @Autowired
    private IMT4AccountService imt4AccountService;

    @Autowired
    private Nj4xConfig nj4xConfig;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.error("同步MT4服务器时差");

        Long startTime = System.currentTimeMillis();

        //获取所有的MT4账户
        QueryWrapper<Mt4Account> queryWrapper = new QueryWrapper<Mt4Account>().select("telephone", "user", "broker", "password", "status", "time_zone_offset");
        List<Mt4Account> mt4AccountList = imt4AccountService.list(queryWrapper);

        //对每一个账户进行处理
        for (Mt4Account mt4Account :
                mt4AccountList) {

            Mt4c mt4c = new Mt4c(nj4xConfig, new Broker(mt4Account.getBroker()), mt4Account.getUser() + "@" + mt4Account.getBroker() + "SynTimeZoneOffset", mt4Account.getPassword());

            int connectionTimes = 0;
            int maxTimes = 4;
            boolean retry = false;

            //最多可以(maxTimes-1)次
            do {
                connectionTimes++;
                try {
                    try {
                        if (retry) {
                            log.error(mt4Account.getUser() + "@" + mt4Account.getBroker() + "第" + (connectionTimes - 1) + "次重连");
                        }

                        //登陆MT4服务器,该任务只需要获取服务器时间和GMT时间之间的差，所以只需要获取今日订单
                        mt4c.connect(Strategy.HistoryPeriod.TODAY);

                        // hide from market watch to minimize traffic/CPU usage,降低服务器不必要的性能消耗
                        for (String s : mt4c.getSymbols()) {
                            mt4c.symbolSelect(s, false);
                        }

                        //获取MT4服务器和GMT时间之间的时差
                        int offset = mt4c.serverTimeGMTOffset() / 3600;

                        if (offset != mt4Account.getTimeZoneOffset()) {
                            mt4Account.setTimeZoneOffset(offset);

                            //更新账户的资产数据
                            UpdateWrapper<Mt4Account> updateWrapper = new UpdateWrapper<Mt4Account>().eq("telephone", mt4Account.getTelephone()).eq("user", mt4Account.getUser());
                            imt4AccountService.update(mt4Account, updateWrapper);
                        }

                    } finally {
                        mt4c.close(true);
                    }
                } catch (NJ4XMaxNumberOfTerminalsExceededException e) {
                    //如果是连接过程报错，则尝试重新连接
                    retry = true;
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (retry && (connectionTimes < maxTimes));


        }
        Long endTime = System.currentTimeMillis();
        log.error("同步MT4服务器时差：" + (endTime - startTime) / 1000 + " sec");

    }
}
