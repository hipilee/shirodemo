package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.aotfx.mobile.dao.entity.ActiveOrderBean;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.IActiveOrderService;
import com.aotfx.mobile.service.nj4x.IMT4AccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfx.Broker;
import com.jfx.ErrNoOrderSelected;
import com.jfx.SelectionPool;
import com.jfx.SelectionType;
import com.jfx.strategy.Strategy;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Vector;

@DisallowConcurrentExecution
public class SynActiveOrders implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(SynActiveOrders.class);

    @Autowired
    private IActiveOrderService iActiveOrderService;

    @Autowired
    private IMT4AccountService imt4AccountService;

    @Autowired
    private Nj4xConfig nj4xConfig;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("同步持仓订单");


        Long startTime = System.currentTimeMillis();

        QueryWrapper<Mt4Account> queryWrapper = new QueryWrapper<Mt4Account>().select("telephone", "user", "broker", "password", "status");
        List<Mt4Account> mt4AccountList = imt4AccountService.list(queryWrapper);

        //循环获取mt4账户和密码
        for (Mt4Account mt4Account :
                mt4AccountList) {

            Mt4c mt4c = new Mt4c(nj4xConfig, new Broker(mt4Account.getBroker()), mt4Account.getUser() + "@" + mt4Account.getBroker() + "SynActiveOrders", mt4Account.getPassword());

            int connectionTimes = 0;
            int maxTimes = 4;
            boolean retry = false;

            //最多可以两次
            do {
                connectionTimes++;
                try {
                    try {
                        if (retry) {
                            System.out.println(mt4Account.getUser() + "@" + mt4Account.getBroker() + "第" + (connectionTimes - 1) + "次重连");
                        }
                        mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);

                        // hide from market watch to minimize traffic/CPU usage
                        for (String s : mt4c.getSymbols()) {
                            mt4c.symbolSelect(s, false);
                        }
                        Vector<ActiveOrderBean> activeOrderBeanVector = new Vector<>();

                        activeOrderBeanVector = assembleActiveOrders(mt4Account, mt4c, activeOrderBeanVector);
                        databaseCRUD(activeOrderBeanVector, mt4Account);
                    } finally {
                        mt4c.close(true);
                    }
                } catch (Exception e) {
                    //如果连接过程报错，则尝试重新连接
                    retry = true;
                    e.printStackTrace();
                }
            } while (retry && (connectionTimes < maxTimes));


        }
        Long endTime = System.currentTimeMillis();
        _log.error("同步持仓订单耗时：" + (endTime - startTime) / 1000 + " sec");

    }


    private Vector<ActiveOrderBean> assembleActiveOrders(Mt4Account mt4Account, Mt4c mt4c, Vector<ActiveOrderBean> activeOrderBeanVector) {

        //Find the reality account number and telephone.
        String user = mt4c.getMt4User().substring(0, mt4c.getMt4User().indexOf("@"));

        Long telephone = mt4Account.getTelephone();

        int ordersCount = mt4c.ordersHistoryTotal();
        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_TRADES)) {
                try {

                    //Add every order into vector.
                    ActiveOrderBean activeOrderBean = new ActiveOrderBean(telephone, user, mt4c.orderTicketNumber(), mt4c.orderOpenTime(), mt4c.orderType(),
                            mt4c.orderLots(), mt4c.orderSymbol(), mt4c.orderOpenPrice(), mt4c.orderStopLoss(), mt4c.orderTakeProfit(), mt4c.orderClosePrice(),
                            mt4c.orderComment(), mt4c.orderCommission(), 0.0, mt4c.orderSwap(), mt4c.orderProfit());
                    activeOrderBeanVector.add(activeOrderBean);
                } catch (ErrNoOrderSelected errNoOrderSelected) {
                    errNoOrderSelected.printStackTrace();
                }
            }
        }

        return activeOrderBeanVector;
    }

    private void databaseCRUD(Vector<ActiveOrderBean> activeOrderBeanVector, Mt4Account mt4User) {

        //delete all orders of the current account according to the columns telephone and user.
        QueryWrapper<ActiveOrderBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user", mt4User.getUser()).eq("telephone", mt4User.getTelephone());
        iActiveOrderService.remove(queryWrapper);

        //insert all orders of active orders into database by batch method.
        iActiveOrderService.saveBatch(activeOrderBeanVector);

    }


}
