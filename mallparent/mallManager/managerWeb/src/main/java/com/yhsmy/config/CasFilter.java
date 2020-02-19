package com.yhsmy.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auth 李正义
 * @date 2020/2/8 14:22
 **/
@WebFilter(value = "casFilter", urlPatterns = "/*")
public class CasFilter implements Filter {

    @Value ("${cas.server.loginUrl}")
    private String loginUrl;

    @Value ("${cas.server.returnUrlKey}")
    private String returnUrlKey;

    @Value ("${cas.server.logoutUrl}")
    private String logoutUrl;

    @Value ("${cas.server.ignorePrefixs}")
    private String ignorePrefixs;

    @Value ("${cas.server.ignoreSuffixs}")
    private String ignoreSuffixs;

    @Override
    public void init (FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String uri = request.getRequestURI().toLowerCase();
        this.doFilter (uri, ignorePrefixs, request, response, filterChain );
        this.doFilter (uri, ignoreSuffixs, request, response, filterChain );
        //未登录
        String accept = request.getHeader("X-Requested-With");
        if(StringUtils.isNotBlank (accept) && accept.indexOf("XMLHttpRequest") != -1) {
            // ajax访问
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().print("<script>alert('请重新登录！');</script>");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        } else {
            // 普通访问

        }
        //已登录
        filterChain.doFilter (request, response);
    }

    private void doFilter(String uri, String ignores, HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) throws IOException, ServletException {
        if(StringUtils.isNotBlank (ignores)) {
            String[] ignoreArr = StringUtils.split (ignores, ",");
            for(String ignore: ignoreArr) {
                if(uri.indexOf (ignore) <= -1) {
                    continue;
                }
                filterChain.doFilter (request, response);
                return;
            }
        }
    }

    @Override
    public void destroy () {

    }
}
