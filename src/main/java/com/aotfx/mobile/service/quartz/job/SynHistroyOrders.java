//package com.aotfx.mobile.service.quartz.job;
//
//import com.aotfx.mobile.dao.entity.HistoryOrderBean;
//import com.aotfx.mobile.dao.entity.Mt4Account;
//import com.aotfx.mobile.manager.Mt4c;
//import com.aotfx.mobile.service.nj4x.IHistroyOrderService;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.jfx.Broker;
//import com.jfx.ErrNoOrderSelected;
//import com.jfx.SelectionPool;
//import com.jfx.SelectionType;
//import com.jfx.strategy.Strategy;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Vector;
//
//@DisallowConcurrentExecution
//public class SynHistroyOrders implements BaseJob {
//    private static Logger _log = LoggerFactory.getLogger(SynHistroyOrders.class);
//
//    @Autowired
//    IHistroyOrderService iHistroyOrderService;
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        _log.error("同步历史订单");
//
//        Mt4Account mt4User = new Mt4Account(15708470013L, "80012391", "Ava-Real 5", "Lxtcfx8793", 1);
//        //循环获取mt4账户和密码
//        for (int i = 0; i < 10; i++) {
//
//        }
//
//
//        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker(mt4User.getBroker()), mt4User.getUser() + "@" + mt4User.getBroker() + "SynHistroyOrders", mt4User.getPassword());
//
//
//        try {
//            try {
//                mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);
//
//                Vector<HistoryOrderBean> histroyOrderBeanVector = new Vector<>();
//                assembleHistroyOrders(mt4c, histroyOrderBeanVector);
//
//                databaseCRUD(histroyOrderBeanVector);
//            } finally {
//                mt4c.close(true);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private Vector<HistoryOrderBean> assembleHistroyOrders(Mt4c mt4c, Vector<HistoryOrderBean> histroyOrderBeanVector) {
//        String user = mt4c.getMt4User().substring(0, mt4c.getMt4User().indexOf("@"));
//        int ordersCount = mt4c.ordersHistoryTotal();
//
//        for (int i = 0; i < ordersCount; i++) {
//            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_HISTORY)) {
//                try {
//
//                    //将每一个持仓订单，加入到vector
//                    HistoryOrderBean histroyOrderBean = new HistoryOrderBean(mt4c.orderTicketNumber(), mt4c.orderOpenTime(), mt4c.orderType(),
//                            mt4c.orderLots(), mt4c.orderSymbol(), mt4c.orderOpenPrice(), mt4c.orderStopLoss(), mt4c.orderTakeProfit(),
//                            mt4c.orderCloseTime(), mt4c.orderClosePrice(), mt4c.orderCommission(), 0.0, mt4c.orderSwap(), mt4c.orderProfit(),
//                            mt4c.orderComment(), user);
//                    histroyOrderBeanVector.add(histroyOrderBean);
//                } catch (ErrNoOrderSelected errNoOrderSelected) {
//                    errNoOrderSelected.printStackTrace();
//                }
//            }
//        }
//        return histroyOrderBeanVector;
//    }
//
//    private void databaseCRUD(Vector<HistoryOrderBean> histroyOrderBeanVector) {
//        for (HistoryOrderBean hisOrder :
//                histroyOrderBeanVector) {
//            //判断该订单是否在历史里面已经存在，我们现在每一次获取的历史订单是前三天的；所以现在获取的历史订单有可能已经在历史订单中了。
//
//            //构建判断逻辑
//            QueryWrapper<HistoryOrderBean> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("user", hisOrder.getUser()).eq("order_number", hisOrder.getOrderNumber());
//            if (iHistroyOrderService.count(queryWrapper) < 1) {
//                iHistroyOrderService.save(hisOrder);
//            }
//        }
//    }
//}
//
