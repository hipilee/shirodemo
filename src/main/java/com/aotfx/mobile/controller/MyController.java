package com.aotfx.mobile.controller;

import com.aotfx.mobile.common.utils.SysResult;
import com.aotfx.mobile.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by myz on 2017/7/4.
 */
@Controller
public class MyController extends AotfxBaseController {

    @Autowired
    private UserService baseService;

    private final Logger logger = LoggerFactory.getLogger(MyController.class);

    @ResponseBody
    @RequestMapping(value = "/doLogin", method = {RequestMethod.POST, RequestMethod.GET})
    public SysResult doLogin(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam("password") String password) {
        // 创建Subject实例
        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession(true);
        String sessionTelephone = session.getAttribute("telephone") != null ? (String) session.getAttribute("telephone") : null;
        System.out.println(session.getId());

        // 判断当前用户是否登录
        if (!currentUser.isAuthenticated() || !telephone.equals(sessionTelephone)) {
            logger.error("没有登录");
            try {
                System.out.println(session.getAttribute("telephone") + "=====================================================登录中");

                // 将用户名及密码封装到UsernamePasswordToken
                UsernamePasswordToken token = new UsernamePasswordToken(telephone, password);
                currentUser.login(token);

                //在session中添加标记用户的电话号码
                session.setAttribute("telephone", telephone);

            } catch (AuthenticationException e) {
                return SysResult.build(10, "登录失败", null);
            }
        }
        return SysResult.build(11, "登录成功", null);

    }


    @RequestMapping("/doRegister1")
    @ResponseBody
    public SysResult doRegister1(@RequestParam(value = "telephone") String telephone, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {

        System.out.print("telephone " + telephone);

        boolean result = baseService.registerUser(telephone, username, password);
        if (result) {
            return new SysResult<Object>(10, "注册成功", "");
        }
        return new SysResult<Object>(11, "信息有误，检查后重试", "");
    }

    @RequestMapping(value = "/login")
    public String login() {
        logger.info("login() 方法被调用");
        return "loginPage.html";
    }

    //用户退出
    @ResponseBody
    @RequestMapping("/logout")
    public SysResult logout() {

        SysResult sysResult;

        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            sysResult = SysResult.build(11,"退出失败",null);
            return sysResult;
        }
        sysResult = SysResult.build(10,"退出成功",null);
        return sysResult;
    }

    @RequestMapping(value = "/register")
    public String register() {
        logger.info("register() 方法被调用");
        return "registerPage.html";
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        logger.info("hello() 方法被调用");
        return "helloPage.html";
    }

    @RequestMapping(value = "/JobManager")
    public String JobManager() {
        logger.info("JobManager() 方法被调用");
        return "JobManagerPage.html";
    }
}
