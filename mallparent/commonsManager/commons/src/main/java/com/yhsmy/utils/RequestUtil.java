package com.yhsmy.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
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

    public static String[] getBroswers (HttpServletRequest request) {
        String agent = request.getHeader ("User-Agent");
        if (StringUtils.isBlank (agent)) {
            return new String[]{"", ""};
        }

        UserAgent userAgent = UserAgent.parseUserAgentString (agent);
        String browser = userAgent.getBrowser ().getName (),
                bVersion = userAgent.getBrowserVersion ().getVersion ();

        return new String[]{StringUtils.isEmpty (browser) ? "" : browser,
                StringUtils.isEmpty (bVersion) ? "" : bVersion};
    }

    /**
     * @param ip ip地址
     * @return string[]{国家·省份/市,经度|纬度}
     */
    public static String[] getCityInfo (String ip) {
        try {
            Resource resource = new ClassPathResource ("geolite/GeoLite2-ASN.mmdb");
            DatabaseReader dbReader = new DatabaseReader.Builder (resource.getFile ()).build ();
            InetAddress inetAddress = InetAddress.getByName (ip);
            CityResponse response = dbReader.city (inetAddress);
            Country country = response.getCountry (); // 获取国家信息
            String countryName = country.getNames ().get ("zh-CN");
            if(StringUtils.isEmpty (countryName)) {
                countryName = "中国";
            }
            Subdivision subdivision = response.getMostSpecificSubdivision (); // 获取省份信息
            String subName = subdivision.getNames ().get ("zh-CN");
            if(StringUtils.isEmpty (subName)) {
                subName = "四川省";
            }
            City city = response.getCity (); // 获取城市
            String cityName = city.getNames ().get ("zh-CN");
            if(StringUtils.isEmpty (cityName)) {
                cityName = "成都市";
            }
            Location location = response.getLocation (); // 获取经纬度
            String latitude = String.valueOf (location.getLatitude ()); // 纬度
            String longitude = String.valueOf (location.getLongitude ()); // 经度
            return new String[]{countryName + "·" + subName + "/" + cityName, longitude + "|" + latitude};
        } catch (Exception e) {
        }
        return new String[]{"中国·四川省/成都市", "104.05|30.68"};
    }


}
