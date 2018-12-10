package com.aotfx.mobile.controller;

import com.aotfx.mobile.common.utils.RSA;
import com.aotfx.mobile.common.utils.SysResult;
import com.aotfx.mobile.dao.entity.User;
import com.aotfx.mobile.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.codec.binary.Base64;
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
    private IUserService iUserService;

    private final Logger logger = LoggerFactory.getLogger(MyController.class);

    @ResponseBody
    @RequestMapping(value = "/doLogin", method = {RequestMethod.POST, RequestMethod.GET})
    public SysResult doLogin(@RequestParam(value = "telephone",required = false) String telephone,
                             @RequestParam(value = "password",required = false) String password, @RequestParam(value = "captcha",required = false) String captcha) {


        // 创建Subject实例
        logger.info("当前用户的telephone:" + telephone);
        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession(true);

        User user = session.getAttribute("user") != null ? (User) session.getAttribute("user") : null;
        logger.info("当前用户的sessionID:" + session.getId());

        String telephoneFromSession;
        try {
            telephoneFromSession = user == null ? null : new String(RSA.decryptByPublicKey(Base64.decodeBase64(user.getEncryptTelephone()), RSA.getAotfxPublicKey()));
        } catch (Exception e) {
            return SysResult.build(-1, "登录失败,服务器内部错误", null);
        }

        // 判断当前用户是否登录
        if (!currentUser.isAuthenticated() || !telephone.equals(telephoneFromSession)) {

            //如果当前回话话有另外一个用户登陆，那么需要先推出当前用户。
            if (!telephone.equals(telephoneFromSession)) {
                currentUser.logout();
                currentUser = SecurityUtils.getSubject();
                session = currentUser.getSession(true);
            }

            logger.error("没有登录");
            try {
                System.out.println(session.getAttribute("telephone") + "=====================================================登录中");


                //查找出当前电话对应在数据库里面的人
                QueryWrapper<User> isExistByTelephone = new QueryWrapper<>();
                String encrypt_telephone;
                try {
                    encrypt_telephone = Base64.encodeBase64String(RSA.encryptByPrivateKey(telephone.getBytes(), RSA.getAotfxPrivateKey()));

                } catch (Exception e) {
                    logger.error("对 " + telephone + " 私钥加密异常！");
                    return SysResult.build(-1, "登录失败,服务器内部错误", null);
                }
                isExistByTelephone.eq("encrypt_telephone", encrypt_telephone);
                final User userFromDb = iUserService.getOne(isExistByTelephone);

                UsernamePasswordToken token;
                if (userFromDb == null) {
                    throw new AuthenticationException("用户不存在！");
                } else {
                    // 将用户名及密码封装到UsernamePasswordToken
                    token = new UsernamePasswordToken(telephone, password);
                }

                currentUser.login(token);

                //在session中添加标记用户的电话号码
                session.setAttribute("user", userFromDb);

            } catch (AuthenticationException e) {
                return SysResult.build(-1, e.getMessage(), null);
            }
        }

        return SysResult.build(10, "登录成功", null);

    }


    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult doRegister(@RequestParam(value = "telephone",required = false) String telephone, @RequestParam(value = "username",required = false) String username, @RequestParam(value = "password",required = false) String password, @RequestParam(value = "captcha",required = false) String captcha) {

        logger.info("注册telephone:" + telephone);

        String result;
        try {
            result = iUserService.registerUser(telephone, username, password, captcha);
            if (result != null && result.equals("注册成功")) {
                return new SysResult<Object>(10, result, "");
            }else{
                return new SysResult<Object>(-1, result, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SysResult<Object>(-1, e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/login")
    public String login() {
        logger.info("login() 方法被调用");
        return "/loginPage";
    }

    //用户退出
    @ResponseBody
    @RequestMapping("/logout")
    public SysResult logout() {

        SysResult sysResult;

        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            sysResult = SysResult.build(11, "退出失败", null);
            return sysResult;
        }
        sysResult = SysResult.build(10, "退出成功", null);
        return sysResult;
    }

    @RequestMapping(value = "/register")
    public String register() {
        logger.info("register() 方法被调用");
        return "/registerPage";
    }

    @RequestMapping(value = "/hello")
    public String hello() {
        logger.info("hello() 方法被调用");
        return "/helloPage";
    }

    @RequestMapping(value = "/JobManager")
    public String JobManager() {
        logger.info("JobManager() 方法被调用");
        return "/JobManagerPage";
    }
}
