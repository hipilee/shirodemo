package com.aotfx.mobile.filter;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


@Component
@WebFilter(urlPatterns = "/*", filterName = "authFilter")
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


//
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        Enumeration<String> s = httpServletRequest.getHeaderNames();
//        while(s.hasMoreElements()){
//            System.out.println("-------------");
//            String ss = s.nextElement();
//            System.out.println(ss);
//            System.out.println(httpServletRequest.getHeader(ss));
//
//        }




        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest)request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
