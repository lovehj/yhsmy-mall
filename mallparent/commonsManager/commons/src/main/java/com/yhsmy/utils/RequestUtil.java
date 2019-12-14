package com.yhsmy.utils;

import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/11/11 20:58
 **/
public class RequestUtil {

    /**
     * 判断是否为ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest (HttpServletRequest request) {
        String requestedWith = request.getHeader ("x-requested-with");
        return requestedWith != null && requestedWith.equalsIgnoreCase ("XMLHttpRequest");
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getIp (HttpServletRequest request) {
        String ip = request.getHeader ("x-forwarded-for");
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getHeader ("Proxy-Client-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getHeader ("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getHeader ("X-Real-IP");
        }
        if (ip == null || ip.length () == 0 || "unknown".equalsIgnoreCase (ip)) {
            ip = request.getRemoteAddr ();
        }
        return ip;
    }

    /**
     * 获取客户端请求的平台
     *
     * @param request
     * @return
     */
    public static String getPlatform (HttpServletRequest request) {
        String agent = request.getHeader ("User-Agent"),
                platform = "pc";

        if (agent.contains ("iPhone") || agent.contains ("iPod") || agent.contains ("iPad")) {
            platform = "ios";
        } else if (agent.contains ("Android") || agent.contains ("Linux")) {
            platform = "android";
        } else if (agent.indexOf ("micromessenger") > 0) {
            platform = "wx";
        }
        return platform;
    }

    public static String[] getBroswers(HttpServletRequest request){
        String agent = request.getHeader ("User-Agent");
        if(StringUtils.isBlank (agent)) {
            return new String[]{"",""};
        }

        UserAgent userAgent = UserAgent.parseUserAgentString (agent);
        String browser = userAgent.getBrowser ().getName (),
                bVersion = userAgent.getBrowserVersion ().getVersion ();

        return new String[]{StringUtils.isEmpty (browser) ? "" : browser,
                StringUtils.isEmpty (bVersion) ? "" : bVersion};
    }



}
