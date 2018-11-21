package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.dao.entity.ActiveOrderBean;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.impl.ActiveOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfx.Broker;
import com.jfx.ErrNoOrderSelected;
import com.jfx.SelectionPool;
import com.jfx.SelectionType;
import com.jfx.net.JFXServer;
import com.jfx.strategy.Strategy;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.util.Vector;

@DisallowConcurrentExecution
public class SynActiveOrders implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(SynActiveOrders.class);

    @Autowired
    private ActiveOrderService activeOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("同步持仓订单");

        Mt4Account mt4User = new Mt4Account(15708470013L, "80012391", "Ava-Real 5", "Lxtcfx8793", 1);
//        Mt4Account mt4User = new Mt4Account(15708470013L, "3815241", "204.8.241.79", "ur4bzys", 1);
        //Get MT4 accounts cycle.
        for (int i = 0; i < 10; i++) {

        }


        Mt4c mt4c = new Mt4c("192.168.1.6", 7788, new Broker(mt4User.getBroker()), mt4User.getUser() + "@" + mt4User.getBroker() + "SynActiveOrders", mt4User.getPassword());


        try {
            try {
                mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);

                Vector<ActiveOrderBean> activeOrderBeanVector = new Vector<>();

                activeOrderBeanVector = assembleActiveOrders(mt4User, mt4c, activeOrderBeanVector);
                databaseCRUD(activeOrderBeanVector, mt4User);
            } finally {
                mt4c.close(true);
                JFXServer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        activeOrderService.remove(queryWrapper);

        //insert all orders of active orders into database by batch method.
        activeOrderService.saveBatch(activeOrderBeanVector);

    }


}
