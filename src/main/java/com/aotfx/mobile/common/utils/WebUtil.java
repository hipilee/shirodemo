package com.aotfx.mobile.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-29 10:56
 */
public class WebUtil {
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
