package com.aotfx.mobile.service.quartz;

import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.quartz.impl.BaseJob;
import com.jfx.Broker;
import com.jfx.ErrNoOrderSelected;
import com.jfx.SelectionPool;
import com.jfx.SelectionType;
import com.jfx.net.JFXServer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SynActiveOrders implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(SynActiveOrders.class);

    public SynActiveOrders() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("同步持仓订单");
        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker("mt4d01.fxcorporate.com"), "3825688", "w3kvmbm");
        try {
            mt4c.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int ordersCount = mt4c.ordersTotal();
        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_TRADES)) {
                try {
                    int type = mt4c.orderType(); // see TradeOperation
                    System.out.println(String.format("=====================Active order #%d, P/L=%f, comments=%s",
                            mt4c.orderTicket(), mt4c.orderProfit(), mt4c.orderComment())
                    );
                } catch (ErrNoOrderSelected errNoOrderSelected) {
                    errNoOrderSelected.printStackTrace();
                }
            }
        }
        try {
            mt4c.close(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFXServer.stop();

    }

    public static void main(String[] args) {
        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker("mt4d01.fxcorporate.com"), "3825688", "w3kvmbm");
        try {
            mt4c.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(mt4c.accountBalance());
    }
}
