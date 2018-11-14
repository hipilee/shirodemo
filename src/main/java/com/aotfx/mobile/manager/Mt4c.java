package com.aotfx.mobile.manager;

import com.aotfx.mobile.beans.HistroyOrderBean;
import com.aotfx.mobile.common.utils.OrderTypeEnum;
import com.jfx.Broker;
import com.jfx.ErrNoOrderSelected;
import com.jfx.SelectionPool;
import com.jfx.SelectionType;
import com.jfx.strategy.Strategy;

import java.util.Vector;

public class Mt4c extends Strategy {
    static {
        System.setProperty("jfx_server_port", "7779");

        //如果程序和terminal server不在同一台电脑上，需要执行编写下面这句代码
        System.setProperty("nj4x_server_host", "192.168.1.8");
    }

    private String termServerHost;
    private int termServerPort;
    private Broker mt4Server;
    private String mt4User;
    private String mt4Password;

    public Mt4c(String termServerHost, int termServerPort, Broker mt4Server, String mt4User, String mt4Password) {
        this.termServerHost = termServerHost;
        this.termServerPort = termServerPort;
        this.mt4Server = mt4Server;
        this.mt4User = mt4User;
        this.mt4Password = mt4Password;
    }

    public void connect() throws Exception {
        this.withHistoryPeriod(Strategy.HistoryPeriod.ALL_HISTORY).connect(termServerHost, termServerPort, mt4Server, mt4User, mt4Password);
    }

    public HistroyOrderBean[] histroyOrders() {
        int ordersHistoryCount = ordersHistoryTotal();
        Vector<HistroyOrderBean> vectorHisOrders = new Vector();

        for (int i = 0; i < ordersHistoryCount; i++) {
            try {
                if (orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_HISTORY) && orderType() < 6) {

                    HistroyOrderBean hisOrder = new HistroyOrderBean();
                    setOrderBean(hisOrder);
                    vectorHisOrders.add(hisOrder);
                }
            } catch (ErrNoOrderSelected errNoOrderSelected) {
                errNoOrderSelected.printStackTrace();
            }
        }
        return vectorHisOrders.toArray(new HistroyOrderBean[vectorHisOrders.size()]);
    }

    private void setOrderBean(HistroyOrderBean histroyOrderBean) throws ErrNoOrderSelected {
        histroyOrderBean.setOrderNumber(orderTicketNumber());
        histroyOrderBean.setType(OrderTypeEnum.values()[orderType()].name());
        histroyOrderBean.setOpenTime(orderOpenTime());
        histroyOrderBean.setSize(orderLots());
//                    private String symbol;
        histroyOrderBean.setSymbol(orderSymbol());
//                    private double openPrice;
//
        histroyOrderBean.setOpenPrice(orderOpenPrice());
//
////    平仓信息
//                    private Date closeTime;
        histroyOrderBean.setCloseTime(orderCloseTime());
//                    private double closePrice;
        histroyOrderBean.setClosePrice(orderClosePrice());
//                    private double commission;
        histroyOrderBean.setCommission(orderCommission());
//                    private double taxes;

//                    private double swap;
        histroyOrderBean.setSwap(orderSwap());
//                    private double profit;
        histroyOrderBean.setProfit(orderProfit());
//                    private String comment;
    }
}
