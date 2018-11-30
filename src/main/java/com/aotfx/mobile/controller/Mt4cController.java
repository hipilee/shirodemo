package com.aotfx.mobile.controller;

import com.aotfx.mobile.common.utils.SysResult;
import com.aotfx.mobile.common.utils.UUID32Util;
import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.aotfx.mobile.dao.entity.AccountAssetBean;
import com.aotfx.mobile.dao.entity.ActiveOrderBean;
import com.aotfx.mobile.dao.entity.HistoryOrderBean;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.manager.Mt4c;
import com.aotfx.mobile.service.nj4x.IAccountAssetService;
import com.aotfx.mobile.service.nj4x.IActiveOrderService;
import com.aotfx.mobile.service.nj4x.IHistoryOrderService;
import com.aotfx.mobile.service.nj4x.IMT4AccountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfx.Broker;
import com.jfx.strategy.Strategy;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.aotfx.mobile.common.utils.SysResult.build;

@Controller
@RequestMapping("/api")
public class Mt4cController extends AotfxBaseController {

    @Autowired
    IMT4AccountService imt4AccountService;

    @Autowired
    IAccountAssetService iAccountAssetService;

    @Autowired
    IActiveOrderService iActiveOrderService;

    @Autowired
    IHistoryOrderService iHistoryOrderService;

    @Autowired
    private Nj4xConfig nj4xConfig;


    //获取当前登录用户，关联的所有MT4账号
    @RequiresUser
    @ResponseBody
    @RequestMapping("/mt4accounts")
    public SysResult<List<Mt4Account>> getMt4accounts() {
        //获取当前登录用户
        Session session = SecurityUtils.getSubject().getSession();
        System.out.println(session.getId());
        Object telephoneVal = session == null ? null : session.getAttribute("telephone");
        Long telephone = telephoneVal == null ? null : Long.parseLong(telephoneVal.toString());

        //获取当前用户的账户列表
        List<Mt4Account> mt4AccountList = imt4AccountService.list(new QueryWrapper<Mt4Account>().eq("telephone", telephone));

        //封装返回结果
        return SysResult.build(10, "MT4账户李彪", mt4AccountList);
    }

    //获取当前登录用户的资产信息，mt4账号是MT4账号
    @ResponseBody
    @RequestMapping("/asset/{mt4}")
    public SysResult<AccountAssetBean> getAssets(@PathVariable(name = "mt4") String mt4) {
        //获取当前登录用户
        Session session = SecurityUtils.getSubject().getSession();
        System.out.println(session.getId());
        Object telephoneVal = session == null ? null : session.getAttribute("telephone");
        Long telephone = telephoneVal == null ? null : Long.parseLong(telephoneVal.toString());

        QueryWrapper<AccountAssetBean> queryWrapper = new QueryWrapper<AccountAssetBean>()
                .eq("telephone", telephone)
                .eq("user", mt4);
        AccountAssetBean accountAssetBean = iAccountAssetService.getOne(queryWrapper);

        //封装返回结果
        SysResult<AccountAssetBean> sysResult;
        if (accountAssetBean != null) {
            sysResult = SysResult.build(10, "账户资产信息", accountAssetBean);
        } else {
            sysResult = SysResult.build(11, "账户不存在", accountAssetBean);
        }
        return sysResult;
    }

    //获取当前登录用户，指定的MT4账户的持仓订单
    @ResponseBody
    @RequestMapping("/activeorders/{mt4}")
    public SysResult<List<ActiveOrderBean>> getActiveOrders(@PathVariable(name = "mt4") String mt4) {
        //获取当前登录用户
        Session session = SecurityUtils.getSubject().getSession();
        System.out.println(session.getId());
        Object telephoneVal = session == null ? null : session.getAttribute("telephone");
        Long telephone = telephoneVal == null ? null : Long.parseLong(telephoneVal.toString());

        QueryWrapper<ActiveOrderBean> queryWrapper = new QueryWrapper<ActiveOrderBean>()
                .eq("telephone", telephone)
                .eq("user", mt4);
        List<ActiveOrderBean> activeOrderBeanList = iActiveOrderService.list(queryWrapper);

        //封装返回结果
        SysResult<List<ActiveOrderBean>> sysResult;
        sysResult = SysResult.build(10, "持仓订单", activeOrderBeanList);
        return sysResult;
    }


    //获取当前登录用户，指定的MT4账户的历史订单，分页获取
    @ResponseBody
    @RequestMapping("/historyorders/{mt4}/{current}/{size}")
    public SysResult<IPage<HistoryOrderBean>> getHistoryOrders(@PathVariable(name = "mt4") String mt4, @Digits(integer = Integer.MAX_VALUE, fraction = 0) @PathVariable(name = "current") Long current, @Digits(integer = Integer.MAX_VALUE, fraction = 0) @PathVariable(name = "size") Long size) {
        //获取当前登录用户
        Session session = SecurityUtils.getSubject().getSession();
        Object telephoneVal = session == null ? null : session.getAttribute("telephone");
        Long telephone = telephoneVal == null ? null : Long.parseLong(telephoneVal.toString());

        IPage<HistoryOrderBean> page = new Page<HistoryOrderBean>(current, size);
        QueryWrapper<HistoryOrderBean> queryWrapper = new QueryWrapper<HistoryOrderBean>()
                .eq("telephone", telephone)
                .eq("user", mt4);

        iHistoryOrderService.page(page, queryWrapper);
        System.out.println(page.getRecords().get(0).getOrderNumber());

        SysResult sysResult;
        sysResult = SysResult.build(10, "历史持仓信息", page);
        return sysResult;
    }


    //获取当前登录用户，指定的MT4账户的历史订单，分页获取
    @ResponseBody
    @RequestMapping(value = "/bindmt4", method = {RequestMethod.POST,RequestMethod.GET})
    public SysResult bindMT4(@RequestParam(name = "mt4") String user, @RequestParam(name = "password") String password, @RequestParam(name = "broker") String broker) {
        //获取当前登录用户
        Session session = SecurityUtils.getSubject().getSession();
        Object telephoneVal = session == null ? null : session.getAttribute("telephone");
        Long telephone = telephoneVal == null ? null : Long.parseLong(telephoneVal.toString());

        //构建MT4账号
        Mt4Account mt4Account = new Mt4Account();
        mt4Account.setTelephone(telephone);
        mt4Account.setUser(user);
        mt4Account.setBroker(broker);
        mt4Account.setPassword(password);

        Mt4c mt4c = new Mt4c(nj4xConfig, new Broker(mt4Account.getBroker()), mt4Account.getUser() + "@" + mt4Account.getBroker() + "bindmt4" + UUID32Util.getOneUUID(), mt4Account.getPassword());


        SysResult sysResult;
        //用该账号去尝试登陆
        try {
            try {
                mt4c.connect(Strategy.HistoryPeriod.TODAY);
            } finally {
                mt4c.close(true);
            }
        } catch (Exception e) {
            //登陆失败
            sysResult = SysResult.build(11, "绑定失败", null);
            return sysResult;
        }

        //登陆成功
        QueryWrapper<Mt4Account> queryWrapper = new QueryWrapper<Mt4Account>()
                .eq("telephone", telephone)
                .eq("user", user);


        if (imt4AccountService.getOne(queryWrapper) == null) {
            mt4Account.setStatus(1);
            imt4AccountService.save(mt4Account);
        } else {
            mt4Account.setStatus(1);
            imt4AccountService.update(mt4Account, queryWrapper);
        }
        sysResult = SysResult.build(10, "绑定成功", null);
        return sysResult;
    }


}
