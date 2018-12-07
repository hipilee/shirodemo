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
import com.jfx.strategy.NJ4XMaxNumberOfTerminalsExceededException;
import com.jfx.strategy.Strategy;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import static java.math.BigDecimal.ROUND_HALF_UP;

@PersistJobDataAfterExecution
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

        //获取所有的MT4账户
        QueryWrapper<Mt4Account> queryWrapper = new QueryWrapper<Mt4Account>().select("user_id", "user", "broker", "password", "status", "time_zone_offset","is_default","create_time","update_time");
        List<Mt4Account> mt4AccountList = imt4AccountService.list(queryWrapper);

        //对每一个账户进行处理
        for (Mt4Account mt4Account :
                mt4AccountList) {

            Mt4c mt4c = new Mt4c(nj4xConfig, new Broker(mt4Account.getBroker()), mt4Account.getUser() + "@" + mt4Account.getBroker() + "SynActiveOrders", mt4Account.getPassword());

            int connectionTimes = 0;
            int maxTimes = 4;
            boolean retry = false;

            //最多可以(maxTimes-1)次
            do {
                connectionTimes++;
                try {
                    try {
                        if (retry) {
                            System.out.println(mt4Account.getUser() + "@" + mt4Account.getBroker() + "第" + (connectionTimes - 1) + "次重连");
                        }

                        //登陆MT4服务器
                        mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);

                        // hide from market watch to minimize traffic/CPU usage
                        for (String s : mt4c.getSymbols()) {
                            mt4c.symbolSelect(s, false);
                        }

                        //组装持仓订单数据
                        Vector<ActiveOrderBean> activeOrderBeanVector = new Vector<>();
                        activeOrderBeanVector = assembleActiveOrders(mt4Account, mt4c, activeOrderBeanVector);

                        //将持仓订单数据写入数据库
                        databaseCRUD(activeOrderBeanVector, mt4Account);
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
        _log.error("同步持仓订单耗时：" + (endTime - startTime) / 1000 + " sec");

    }


    private Vector<ActiveOrderBean> assembleActiveOrders(Mt4Account mt4Account, Mt4c mt4c, Vector<ActiveOrderBean> activeOrderBeanVector) {


        int ordersCount = mt4c.ordersTotal();

        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_TRADES)) {
                try {

                    //Add every order into vector.
                    ActiveOrderBean activeOrderBean = new ActiveOrderBean.Builder(mt4Account.getUserId(), mt4Account.getUser(), mt4c.orderTicketNumber())
                            .openTime(mt4c.orderOpenTime())
                            .type(mt4c.orderType())
                            .size(mt4c.orderLots())
                            .symbol(mt4c.orderSymbol())
                            .openPrice(new BigDecimal(mt4c.orderOpenPrice()).setScale(2, ROUND_HALF_UP))
                            .stopLoss(new BigDecimal(mt4c.orderStopLoss()).setScale(2, ROUND_HALF_UP))
                            .takeProfit(new BigDecimal(mt4c.orderTakeProfit()).setScale(2, ROUND_HALF_UP))
                            .currentPrice(new BigDecimal(mt4c.orderClosePrice()).setScale(2, ROUND_HALF_UP))
                            .commission(new BigDecimal(mt4c.orderCommission()).setScale(2, ROUND_HALF_UP))
                            .taxes(new BigDecimal("0.0").setScale(2, ROUND_HALF_UP))
                            .swap(new BigDecimal(mt4c.orderSwap()).setScale(2, ROUND_HALF_UP))
                            .profit(new BigDecimal(mt4c.orderProfit()).setScale(2, ROUND_HALF_UP))
                            .comment(mt4c.orderComment()).build();

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
        queryWrapper.eq("user", mt4User.getUser()).eq("user_id", mt4User.getUserId());
        iActiveOrderService.remove(queryWrapper);

        //insert all orders of active orders into database by batch method.
        iActiveOrderService.saveBatch(activeOrderBeanVector);
    }
}
