package com.aotfx.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.aotfx.mobile.common.utils.WebUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-29 10:36
 */
public abstract class AotfxBaseController {

    /**
     * 登录认证异常
     */
    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("未登录");
        if (WebUtil.isAjaxRequest(request)) {
            // 输出JSON
            Map<String, Object> map = new HashMap<>();
            map.put("code", "-999");
            map.put("message", "未登录");
            writeJson(map, response);

            return null;
        } else {
            return "redirect:/login";
        }
    }

    /**
     * 权限异常
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("无权限");
        if (WebUtil.isAjaxRequest(request)) {
            // 输出JSON
            Map<String, Object> map = new HashMap<>();
            map.put("code", "12");
            map.put("message", "无权限");
            writeJson(map, response);

            return null;
        } else {
            return "redirect:login";
        }
    }

    /**
     * 输出JSON
     *
     * @param response
     * @author SHANHY
     * @create 2017年4月4日
     */
    private void writeJson(Map<String, Object> map, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSON.toJSONString(map));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

