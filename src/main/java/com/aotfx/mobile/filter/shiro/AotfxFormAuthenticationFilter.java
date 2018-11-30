package com.aotfx.mobile.filter.shiro;

import com.alibaba.fastjson.JSONObject;
import com.aotfx.mobile.common.utils.SysResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-29 14:54
 */
public class AotfxFormAuthenticationFilter extends FormAuthenticationFilter {

    private ServletRequest request;
    private ServletResponse response;

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     *
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Subject currentUser = SecurityUtils.getSubject();

        Session session = currentUser.getSession(true);
        System.out.println(session.getId());

        if (isAjax(request)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");

            //返回值
            SysResult sysResult;
            sysResult = SysResult.build(12,"用户未登陆，请登陆",null);

            //写入
            httpServletResponse.getWriter().write(JSONObject.toJSON(sysResult).toString());
        } else {
            /**
             * 非ajax请求重定向为登录页面，目前这里有个问题，跨域访问的时候X-Requested-With头会丢失。
             */
            httpServletResponse.sendRedirect("/login");
        }
        return false;
    }

//    跨域请求的时候，ajax的X-Requested-With头会丢失，所以是该函数暂时只返回true。
    private boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return Boolean.TRUE;
        }
        return Boolean.TRUE;

//        return Boolean.FALSE;
    }
}

