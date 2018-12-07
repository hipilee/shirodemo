package com.aotfx.mobile.service.quartz.job;

import com.aotfx.mobile.common.utils.AotfxDate;
import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.aotfx.mobile.dao.entity.AccountAssetBean;
import com.aotfx.mobile.dao.entity.HistoryOrderBean;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.IAccountAssetService;
import com.aotfx.mobile.service.nj4x.IHistoryOrderService;
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
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-21 12:46
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SynHistoryOrdersAndAccountAsset implements BaseJob {
    private static Logger log = LoggerFactory.getLogger(SynHistoryOrdersAndAccountAsset.class);

    @Autowired
    IHistoryOrderService iHistoryOrderService;

    @Autowired
    IAccountAssetService iAccountAssetService;

    @Autowired
    private IMT4AccountService imt4AccountService;

    @Autowired
    private Nj4xConfig nj4xConfig;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.error("同步历史订单以及基本资产数据");

        Long startTime = System.currentTimeMillis();

        //从数据库中读取用户
        QueryWrapper<Mt4Account> queryWrapper = new QueryWrapper<Mt4Account>().select("user_id", "user", "broker", "password", "status", "time_zone_offset","is_default","create_time","update_time");
        List<Mt4Account> mt4AccountList = imt4AccountService.list(queryWrapper);

        //循环获取mt4账户和密码
        for (Mt4Account mt4Account :
                mt4AccountList) {

            //同步数据
            synData(mt4Account);
        }

        Long endTime = System.currentTimeMillis();
        log.error("同步历史订单和资产数据耗时：" + (endTime - startTime) / 1000 + " sec");
    }

    private void synData(Mt4Account mt4Account) {
        Mt4c mt4c = new Mt4c(nj4xConfig, new Broker(mt4Account.getBroker()), mt4Account.getUser() + "@" + mt4Account.getBroker() + "SynHistoryOrdersAndAccountAsset", mt4Account.getPassword());


        int connectionTimes = 0;
        int maxTimes = 4;
        boolean retry = false;

        //最多maxTimes
        do {
            connectionTimes++;
            try {
                try {
                    if (retry) {
                        System.out.println(mt4Account.getUser() + "@" + mt4Account.getBroker() + "第" + (connectionTimes - 1) + "次重连");
                    }

                    //登陆MT4服务器
                    mt4c.connect(Strategy.HistoryPeriod.ALL_HISTORY);

                    //构建准备写入数据库的数据
                    AccountAssetBean accountAssetBean;
                    Vector<HistoryOrderBean> historyOrderBeanVector = new Vector<>();
                    accountAssetBean = assembleData(mt4Account, mt4c, historyOrderBeanVector);

                    //将数据写入数据库
                    databaseCRUD(historyOrderBeanVector, accountAssetBean);
                } finally {
                    mt4c.disconnect();
                    mt4c.close(true);
                }
            } catch (Exception e) {
                //如果连接过程报错，则尝试重新连接一次
                retry = true;
                e.printStackTrace();
            }
        } while (retry && (connectionTimes < maxTimes));


    }

    private AccountAssetBean assembleData(Mt4Account mt4Account, Mt4c mt4c, Vector<HistoryOrderBean> historyOrderBeanVector) throws ErrNoOrderSelected {
        int ordersCount = mt4c.ordersHistoryTotal();

        BigDecimal totalProfit;
        BigDecimal yesterdayProfit = new BigDecimal("0.0");
        BigDecimal deposit = new BigDecimal("0.0");
        BigDecimal withdrawal = new BigDecimal("0.0");
        BigDecimal allOrdersProfit = new BigDecimal("0.0");
        BigDecimal balanceOrdersProfit = new BigDecimal("0.0");

        //取得最后平仓的历史持仓里面的订单
        QueryWrapper<HistoryOrderBean> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", mt4Account.getUserId()).eq("user", mt4Account.getUser()).orderByDesc("close_time").last("limit 1");
        HistoryOrderBean historyOrderBean = iHistoryOrderService.getOne(queryWrapper);

//

        //获取平仓时间最晚的日期
        Date recentDate = null;
        if (historyOrderBean != null) {
            System.out.println(historyOrderBean.getCloseTime());
            recentDate = historyOrderBean.getCloseTime();
        }

        for (int i = 0; i < ordersCount; i++) {
            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_HISTORY)) {
                //whether to be added into history orders
                if (recentDate == null) {
                    //历史持仓为空
                    historyOrderBeanVector.add(construct(mt4Account, mt4c));
                } else if (mt4c.orderCloseTime().compareTo(recentDate) > 0) {
                    historyOrderBeanVector.add(construct(mt4Account, mt4c));
                }

                /* 此处的收益订单的收益和的计算，一定不要忘记计算手续费和隔夜利息。因为不同的做市商对隔夜利息和手续费在订单的显示不一样。
                 * 比如说爱华的隔夜利息是和对应订单显示在一起的作为swap参数显示，而福汇则是单独作为一条balance订单显示*/
                allOrdersProfit = allOrdersProfit.add(new BigDecimal(mt4c.orderProfit())).add(new BigDecimal(mt4c.orderCommission())).add(new BigDecimal(mt4c.orderSwap()));

                /* 订单类型值大于5的都是balance订单。OP_BUY,OP_SELL ,OP_BUYLIMIT ,OP_BUYSTOP ,OP_SELLLIMIT ,OP_SELLSTOP*/
                if (mt4c.orderType() > 5) {
                    balanceOrdersProfit = balanceOrdersProfit.add(new BigDecimal(mt4c.orderProfit())).add(new BigDecimal(mt4c.orderCommission())).add(new BigDecimal(mt4c.orderSwap()));
                    //计算存款和出金
                    if (mt4c.orderProfit() > 0.0) {
                        deposit = deposit.add(new BigDecimal(mt4c.orderProfit()));
                    } else {
                        withdrawal = withdrawal.add(new BigDecimal(mt4c.orderProfit()));
                    }
                }

                /* 昨日收益计算出mt4服务器的时间，由于周六周天服务器不更新服务器时间，
                所以在这种情况下需要通过获取GMT+8区的时间， 然后自己手动计算服务器时间。*/
                int offset=mt4Account.getTimeZoneOffset();

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.HOUR, -8+offset);// 24小时制
                Date mt4Date = cal.getTime();



                //计算昨日收益的时候只能计算非balance订单的orderCommission()+orderTaxes()+orderProfit().
                if (AotfxDate.differentDays(mt4Date, mt4c.orderCloseTime()) == -1&&mt4c.orderType()<6) {
                    yesterdayProfit = yesterdayProfit.add(new BigDecimal(mt4c.orderProfit())).add(new BigDecimal(mt4c.orderCommission())).add(new BigDecimal(mt4c.orderSwap()));
                }
            }
        }
        //计算总收益
        totalProfit = allOrdersProfit.subtract(balanceOrdersProfit).setScale(2, ROUND_HALF_UP);


        AccountAssetBean accountAssetBean = new AccountAssetBean.Builder(mt4Account.getUserId(), mt4Account.getUser()).
                balance(new BigDecimal(mt4c.accountBalance()).setScale(2, ROUND_HALF_UP)).
                credit(new BigDecimal(mt4c.accountCredit()).setScale(2, ROUND_HALF_UP)).
                deposit(deposit.setScale(2, ROUND_HALF_UP)).
                equity(new BigDecimal(mt4c.accountEquity()).setScale(2, ROUND_HALF_UP)).
                free(new BigDecimal(mt4c.accountFreeMargin()).setScale(2, ROUND_HALF_UP)).
                margin(new BigDecimal(mt4c.accountMargin()).setScale(2, ROUND_HALF_UP)).
                profitLoss(totalProfit.setScale(2, ROUND_HALF_UP)).
                yesterdayProfitLoss(yesterdayProfit.setScale(2, ROUND_HALF_UP)).
                withdrawal(withdrawal.setScale(2, ROUND_HALF_UP)).build();

        return accountAssetBean;
    }

    private void databaseCRUD(Vector<HistoryOrderBean> histroyOrderBeanVector, AccountAssetBean accountAssetBean) {

        //更新账户的资产数据
        QueryWrapper<AccountAssetBean> queryWrapper = new QueryWrapper<AccountAssetBean>().eq("user_id", accountAssetBean.getUserId()).eq("user", accountAssetBean.getUser());
        if(iAccountAssetService.getOne(queryWrapper)==null){
            iAccountAssetService.save(accountAssetBean);
        }else{
        iAccountAssetService.update(accountAssetBean, queryWrapper);}

        //批量更新历史持仓订单信息
        iHistoryOrderService.saveBatch(histroyOrderBeanVector);
    }

    private HistoryOrderBean construct(Mt4Account mt4Account, Mt4c mt4c) throws ErrNoOrderSelected {
        HistoryOrderBean historyOrderBean = new HistoryOrderBean.Builder(mt4Account.getUserId(), mt4Account.getUser(), mt4c.orderTicketNumber())
                .openTime(mt4c.orderOpenTime())
                .type(mt4c.orderType()).size(mt4c.orderLots()).symbol(mt4c.orderSymbol())
                .openPrice(new BigDecimal(mt4c.orderOpenPrice()).setScale(5, ROUND_HALF_UP))
                .stopLoss(new BigDecimal(mt4c.orderStopLoss()).setScale(5, ROUND_HALF_UP))
                .takeProfit(new BigDecimal(mt4c.orderTakeProfit()).setScale(5, ROUND_HALF_UP))
                .closeTime(mt4c.orderCloseTime())
                .closePrice(new BigDecimal(mt4c.orderClosePrice()).setScale(5, ROUND_HALF_UP))
                .commission(new BigDecimal(mt4c.orderCommission()).setScale(2, ROUND_HALF_UP))
                .taxes(new BigDecimal("0.0").setScale(2, ROUND_HALF_UP))
                .swap(new BigDecimal(mt4c.orderSwap()).setScale(2, ROUND_HALF_UP))
                .profit(new BigDecimal(mt4c.orderProfit()).setScale(2, ROUND_HALF_UP))
                .comment(mt4c.orderComment()).build();
        return historyOrderBean;
    }
}
