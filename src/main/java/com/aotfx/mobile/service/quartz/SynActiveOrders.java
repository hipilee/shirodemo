package com.aotfx.mobile.service.quartz;

import com.aotfx.mobile.beans.ActiveOrderBean;
import com.aotfx.mobile.common.utils.OrderTypeEnum;
import com.aotfx.mobile.dao.nj4x.OrderMapper;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Vector;

public class SynActiveOrders implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(SynActiveOrders.class);

    @Autowired
    private OrderMapper orderMapper;

    public SynActiveOrders() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("同步持仓订单");

        //循环获取mt4账户和密码
        for(int i =0;i<10;i++){

        }
        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker("mt4d01.fxcorporate.com"), "3825688", "w3kvmbm");
        try {
            mt4c.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int ordersCount = mt4c.ordersTotal();
        Vector<ActiveOrderBean> activeOrderBeanVector = new Vector<>();
        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_TRADES)) {
                try {

                    //将每一个持仓订单，加入到vector中
                    String orderType = OrderTypeEnum.values()[mt4c.orderType()].name();
                    activeOrderBeanVector.add(new ActiveOrderBean("3825688",mt4c.orderTicketNumber(),mt4c.orderOpenTime(), orderType,
                            mt4c.orderLots(),mt4c.orderSymbol(),mt4c.orderOpenPrice(),mt4c.orderStopLoss(),mt4c.orderTakeProfit(),mt4c.orderClosePrice(),
                            mt4c.orderComment(),mt4c.orderCommission(),mt4c.orderSwap(),mt4c.orderProfit()));


                } catch (ErrNoOrderSelected errNoOrderSelected) {
                    errNoOrderSelected.printStackTrace();
                }
            }
        }

        orderMapper.deleteActiveOrdersByUser("3825688");
        orderMapper.insertActiveOrdersBatch(activeOrderBeanVector);

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
