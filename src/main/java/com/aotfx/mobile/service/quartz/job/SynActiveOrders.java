package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.dao.entity.ActiveOrderBean;
import com.aotfx.mobile.dao.entity.Mt4User;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.impl.ActiveOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfx.Broker;
import com.jfx.ErrNoOrderSelected;
import com.jfx.SelectionPool;
import com.jfx.SelectionType;
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

        Mt4User mt4User = new Mt4User("80012391", "Ava-Real 5", "Lxtcfx8793", 1, "15708470013");
        //循环获取mt4账户和密码
        for (int i = 0; i < 10; i++) {

        }


        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker(mt4User.getBroker()), mt4User.getUser() + "@" + mt4User.getBroker()+"SynActiveOrders", mt4User.getPassword());

        try {
            mt4c.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Vector<ActiveOrderBean> activeOrderBeanVector = new Vector<>();

        activeOrderBeanVector = assembleActiveOrders(mt4c, activeOrderBeanVector);
        databaseCRUD(activeOrderBeanVector,mt4User);


        try {
            mt4c.close(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Vector<ActiveOrderBean> assembleActiveOrders(Mt4c mt4c, Vector<ActiveOrderBean> activeOrderBeanVector) {
        String user = mt4c.getMt4User().substring(0, mt4c.getMt4User().indexOf("@"));
        int ordersCount = mt4c.ordersHistoryTotal();

        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_TRADES)) {
                try {

                    //将每一个持仓订单，加入到vector
                    ActiveOrderBean activeOrderBean = new ActiveOrderBean(user, mt4c.orderTicketNumber(), mt4c.orderOpenTime(), mt4c.orderType(),
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

    private void databaseCRUD(Vector<ActiveOrderBean> activeOrderBeanVector,Mt4User mt4User) {
        QueryWrapper<ActiveOrderBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user", mt4User.getUser());
        activeOrderService.remove(queryWrapper);
        for (ActiveOrderBean hisOrder :
                activeOrderBeanVector) {

            activeOrderService.saveBatch(activeOrderBeanVector);
        }
    }


}
