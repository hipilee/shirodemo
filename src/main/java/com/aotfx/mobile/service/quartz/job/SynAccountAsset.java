package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.dao.entity.AccountAssetBean;
import com.aotfx.mobile.dao.entity.HistroyOrderBean;
import com.aotfx.mobile.dao.entity.Mt4User;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.IAccountAssetService;
import com.jfx.Broker;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Vector;

public class SynAccountAsset implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(SynAccountAsset.class);

    @Autowired
    private IAccountAssetService iAccountAssetService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("同步资产信息");

        Mt4User mt4User = new Mt4User("80012391", "Ava-Real 5", "Lxtcfx8793", 1, "15708470013");
        //循环获取mt4账户和密码
        for (int i = 0; i < 10; i++) {

        }

        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker(mt4User.getBroker()), mt4User.getUser() + "@" + mt4User.getBroker() + "SynHistroyOrders", mt4User.getPassword());

//        private String user;
//        private BigDecimal balance;
//        private BigDecimal equity;
//        private BigDecimal margin;
//        private BigDecimal credit;
//        private BigDecimal free;
//        private BigDecimal totalProfitLoss;
//        private BigDecimal yesterdayProfitLoss;
//        private BigDecimal todayProfitLoss;
//        private BigDecimal deposit;
//        private BigDecimal withdrawal;
        try {
            try {
                mt4c.connect();
                AccountAssetBean accountAssetBean = new AccountAssetBean(mt4User.getUser(),new BigDecimal(mt4c.accountBalance()),new BigDecimal(mt4c.accountEquity()),new BigDecimal(mt4c.accountMargin()),
                        new BigDecimal(mt4c.accountCredit()),new BigDecimal(mt4c.accountFreeMargin()),null,null,null,null,null);

            } finally {
                mt4c.close(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
