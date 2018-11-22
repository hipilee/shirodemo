package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.common.utils.AotfxDate;
import com.aotfx.mobile.dao.entity.AccountAssetBean;
import com.aotfx.mobile.dao.entity.HistoryOrderBean;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.IAccountAssetService;
import com.aotfx.mobile.service.nj4x.IHistoryOrderService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-21 12:46
 */
@DisallowConcurrentExecution
public class SynHistoryOrdersAndAccountAsset implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(SynHistoryOrdersAndAccountAsset.class);

    @Autowired
    IHistoryOrderService iHistoryOrderService;

    @Autowired
    IAccountAssetService iAccountAssetService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("同步历史订单以及基本资产数据");

        Mt4Account mt4User = new Mt4Account(15708470013L, "80012391", "Ava-Real 5", "Lxtcfx8793", 1);
//        Mt4Account mt4User = new Mt4Account(15708470013L, "3815241", "FXCM-USDDemo01", "ur4bzys", 1);

        //循环获取mt4账户和密码
        for (int i = 0; i < 10; i++) {

        }


        Mt4c mt4c = new Mt4c("192.168.1.6", 7788, new Broker(mt4User.getBroker()), mt4User.getUser() + "@" + mt4User.getBroker() + "SynHistoryOrdersAndAccountAsset", mt4User.getPassword());


        try {
            try {
                mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);

                AccountAssetBean accountAssetBean = new AccountAssetBean();
                Vector<HistoryOrderBean> historyOrderBeanVector = new Vector<>();
                assembleHistoryOrders(mt4User, mt4c, historyOrderBeanVector, accountAssetBean);

                //databaseCRUD(historyOrderBeanVector, accountAssetBean);
            } finally {
                mt4c.close(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void assembleHistoryOrders(Mt4Account mt4Account, Mt4c mt4c, Vector<HistoryOrderBean> historyOrderBeanVector, AccountAssetBean accountAssetBean) throws ErrNoOrderSelected {
        int ordersCount = mt4c.ordersHistoryTotal();

        BigDecimal totalProfit;
        BigDecimal yesterdayProfit = new BigDecimal(0.0).setScale(2);
        BigDecimal deposit = new BigDecimal(0.0).setScale(2);
        BigDecimal withdrawal = new BigDecimal(0.0).setScale(2);
        BigDecimal allOrdersProfit = new BigDecimal(0.0).setScale(2);
        BigDecimal balanceOrdersProfit = new BigDecimal(0.0).setScale(2);

        QueryWrapper<HistoryOrderBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("telephone", mt4Account.getTelephone()).eq("user", mt4Account.getUser()).orderByDesc("close_time").last("limit 1");
        HistoryOrderBean historyOrderBean = iHistoryOrderService.getOne(queryWrapper);

        Date recentDate = null;
        if (historyOrderBean != null) {
            System.out.println(historyOrderBean.getCloseTime());
            recentDate = historyOrderBean.getCloseTime();
        }

        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_HISTORY)) {
//                whether to be added into history orders
                if (recentDate == null) {
                    historyOrderBeanVector.add(new HistoryOrderBean(mt4Account.getTelephone(), mt4Account.getUser(), mt4c.orderTicketNumber(), mt4c.orderOpenTime(), mt4c.orderType(),
                            mt4c.orderLots(), mt4c.orderSymbol(), new BigDecimal(mt4c.orderOpenPrice()), new BigDecimal(mt4c.orderStopLoss()), new BigDecimal(mt4c.orderTakeProfit()),
                            mt4c.orderCloseTime(), new BigDecimal(mt4c.orderClosePrice()), new BigDecimal(mt4c.orderCommission()), new BigDecimal(0.0), new BigDecimal(mt4c.orderSwap()), new BigDecimal(mt4c.orderProfit()),
                            mt4c.orderComment()));
                } else if (mt4c.orderCloseTime().compareTo(recentDate) == 0 && historyOrderBean.getOrderNumber() != mt4c.orderTicketNumber()) {
                    historyOrderBeanVector.add(new HistoryOrderBean(mt4Account.getTelephone(), mt4Account.getUser(), mt4c.orderTicketNumber(), mt4c.orderOpenTime(), mt4c.orderType(),
                            mt4c.orderLots(), mt4c.orderSymbol(), new BigDecimal(mt4c.orderOpenPrice()), new BigDecimal(mt4c.orderStopLoss()), new BigDecimal(mt4c.orderTakeProfit()),
                            mt4c.orderCloseTime(), new BigDecimal(mt4c.orderClosePrice()), new BigDecimal(mt4c.orderCommission()), new BigDecimal(0.0), new BigDecimal(mt4c.orderSwap()), new BigDecimal(mt4c.orderProfit()),
                            mt4c.orderComment()));
                } else if (mt4c.orderCloseTime().compareTo(recentDate) > 0) {
                    historyOrderBeanVector.add(new HistoryOrderBean(mt4Account.getTelephone(), mt4Account.getUser(), mt4c.orderTicketNumber(), mt4c.orderOpenTime(), mt4c.orderType(),
                            mt4c.orderLots(), mt4c.orderSymbol(), new BigDecimal(mt4c.orderOpenPrice()), new BigDecimal(mt4c.orderStopLoss()), new BigDecimal(mt4c.orderTakeProfit()),
                            mt4c.orderCloseTime(), new BigDecimal(mt4c.orderClosePrice()), new BigDecimal(mt4c.orderCommission()), new BigDecimal(0.0), new BigDecimal(mt4c.orderSwap()), new BigDecimal(mt4c.orderProfit()),
                            mt4c.orderComment()));
                }

                allOrdersProfit = allOrdersProfit.add(new BigDecimal(mt4c.orderProfit())).add(new BigDecimal(mt4c.orderCommission())).add(new BigDecimal(mt4c.orderSwap()));
//                whether it is balance
                if (mt4c.orderType() > 5) {
                    balanceOrdersProfit = balanceOrdersProfit.add(new BigDecimal(mt4c.orderProfit()));
//                    compute the deposit and withdrawal
                    if (mt4c.orderProfit() > 0.0) {
                        deposit = deposit.add(new BigDecimal(mt4c.orderProfit()));
                    } else {
                        withdrawal = withdrawal.add(new BigDecimal(mt4c.orderProfit()));
                    }
                }

//                yesterday profit
                Date currentDate = mt4c.timeCurrent();
                if (AotfxDate.differentDays(currentDate, mt4c.orderCloseTime()) == -1) {
                    yesterdayProfit = yesterdayProfit.add(new BigDecimal(mt4c.orderProfit()));
                }
            }
        }
//       Compute the total profit.
        totalProfit = allOrdersProfit.subtract(balanceOrdersProfit).setScale(2, ROUND_HALF_UP);

        accountAssetBean = new AccountAssetBean.Builder(mt4Account.getTelephone(), mt4Account.getUser()).
                balance(new BigDecimal(mt4c.accountBalance()).setScale(2, ROUND_HALF_UP)).
                credit(new BigDecimal(mt4c.accountCredit()).setScale(2, ROUND_HALF_UP)).
                deposit(new BigDecimal(mt4c.accountBalance()).setScale(2, ROUND_HALF_UP)).
                equity(new BigDecimal(mt4c.accountEquity()).setScale(2, ROUND_HALF_UP)).
                free(new BigDecimal(mt4c.accountFreeMargin()).setScale(2, ROUND_HALF_UP)).
                margin(new BigDecimal(mt4c.accountMargin()).setScale(2, ROUND_HALF_UP)).
                profitLoss( totalProfit.setScale(2, ROUND_HALF_UP)).
                yesterdayProfitLoss( yesterdayProfit.setScale(2, ROUND_HALF_UP)).
                withdrawal( withdrawal.setScale(2, ROUND_HALF_UP)).build();
        databaseCRUD(null, accountAssetBean);
    }

    private void databaseCRUD(Vector<HistoryOrderBean> histroyOrderBeanVector, AccountAssetBean accountAssetBean) {
        iAccountAssetService.update(accountAssetBean, new QueryWrapper<AccountAssetBean>().eq("telephone", accountAssetBean.getTelephone()).eq("user", accountAssetBean.getUser()));

//        for (HistoryOrderBean hisOrder :
//                histroyOrderBeanVector) {
//
//
//            //构建判断逻辑
//            QueryWrapper<HistoryOrderBean> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("user", hisOrder.getUser()).eq("order_number", hisOrder.getOrderNumber());
//            if (iHistoryOrderService.count(queryWrapper) < 1) {
//                iHistoryOrderService.save(hisOrder);
//            }
//        }
    }

    public static void main(String[] args) {
        Mt4Account mt4User = new Mt4Account(15708470013L, "80012391", "Ava-Real 5", "Lxtcfx8793", 1);

        //循环获取mt4账户和密码
        for (int i = 0; i < 10; i++) {

        }


        Mt4c mt4c = new Mt4c("192.168.1.6", 7788, new Broker(mt4User.getBroker()), mt4User.getUser() + "@" + mt4User.getBroker() + "SynHistoryOrdersAndAccountAsset", mt4User.getPassword());


        try {
            try {
                mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);
                int ordersCount = mt4c.ordersHistoryTotal();

                BigDecimal totalProfit;
                BigDecimal yesterdayProfit = new BigDecimal("0.0");
                BigDecimal todayProfit = new BigDecimal("0.0");
                BigDecimal deposit = new BigDecimal("0.0");
                BigDecimal withdrawal = new BigDecimal("0.0");

                BigDecimal allOrdersProfit = new BigDecimal("0.0");
                BigDecimal balanceOrdersProfit = new BigDecimal("0.0");
                for (int i = 0; i < ordersCount; i++) {
                    if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_HISTORY)) {
//                whether to be added into history orders
                        /*code*/
                        allOrdersProfit = allOrdersProfit.add(new BigDecimal(mt4c.orderProfit())).add(new BigDecimal(mt4c.orderCommission())).add(new BigDecimal(mt4c.orderSwap()));
                        System.out.println("allOrdersProfit " + allOrdersProfit);
//                whether it is balance
                        if (mt4c.orderType() > 5) {
                            balanceOrdersProfit = balanceOrdersProfit.add(new BigDecimal(mt4c.orderProfit()));
//                    compute the deposit and withdrawal
                            if (mt4c.orderProfit() > 0.0) {
                                deposit = deposit.add(new BigDecimal(mt4c.orderProfit()));
                            } else {
                                withdrawal = withdrawal.add(new BigDecimal(mt4c.orderProfit()));
                            }
                        }

//                yesterday profit
                        Date currentDate = mt4c.timeCurrent();
                        if (AotfxDate.differentDays(currentDate, mt4c.orderCloseTime()) == -1) {
                            yesterdayProfit = yesterdayProfit.add(new BigDecimal(mt4c.orderProfit()));
                        }
                    }
                }
//       Compute the total profit.
                totalProfit = allOrdersProfit.subtract(balanceOrdersProfit);
                System.out.println("allOrdersProfit " + allOrdersProfit);
                System.out.println("balanceOrdersProfit " + balanceOrdersProfit);
                System.out.println("totalProfit " + totalProfit);
                System.out.println("deposit " + deposit);
                System.out.println("withdrawal " + withdrawal);
                System.out.println("yesterdayProfit " + yesterdayProfit);

            } finally {
                mt4c.close(true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
