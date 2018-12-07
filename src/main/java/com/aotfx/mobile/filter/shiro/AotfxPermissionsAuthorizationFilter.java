package com.aotfx.mobile.filter.shiro;

import com.alibaba.fastjson.JSONObject;
import com.aotfx.mobile.common.utils.SysResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-29 16:57
 */

public class AotfxPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("onPreHandle");
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {


            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            Subject currentUser = SecurityUtils.getSubject();

            Session session = currentUser.getSession(true);
            System.out.println(session.getId());

            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");

            //返回值
            SysResult sysResult;
            sysResult = SysResult.build(13, "用户未授权", null);

            //写入
            httpServletResponse.getWriter().write(JSONObject.toJSON(sysResult).toString());

        return false;
    }

}
