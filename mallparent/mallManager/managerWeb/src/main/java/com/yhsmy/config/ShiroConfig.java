package com.yhsmy.config;

import com.yhsmy.IConstant;
import com.yhsmy.config.shiro.realm.UserRelam;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.RandomSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @auth 李正义
 * @date 2019/11/7 11:26
 **/
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.database}")
    private int database;

    @Bean
    public SecurityManager securityManager () {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager ();
        webSecurityManager.setSessionManager (sessionManager ());
        webSecurityManager.setRememberMeManager (rememberMeManager ());
        webSecurityManager.setCacheManager (redisCacheManager ());
        webSecurityManager.setRealm (userRelam ());
        return webSecurityManager;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager () {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager ();
        rememberMeManager.setCipherKey (Base64.decode ("jt4H4FpBRaZcF118psqViA=="));
        return rememberMeManager;
        //KeyGenerator keygen = KeyGenerator.getInstance ("AES");
        //SecretKey deskey = keygen.generateKey ();
        // System.out.println ("base64= "+Base64.encodeToString (deskey.getEncoded ()));
    }

    @Bean
    public RedisCacheManager redisCacheManager () {
        RedisCacheManager redisCacheManager = new RedisCacheManager ();
        redisCacheManager.setRedisManager (redisManager ());
        return redisCacheManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager () {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager ();
        webSessionManager.setSessionIdCookieEnabled (true);
        webSessionManager.setSessionIdCookie (sessionCookie ());
        webSessionManager.setGlobalSessionTimeout (IConstant.GLOBAL_SESSION_TIMEOUT);
        webSessionManager.setDeleteInvalidSessions (true);
        webSessionManager.setSessionDAO (sessionDAO ());
        return webSessionManager;
    }

    @Bean
    public SimpleCookie sessionCookie () {
        SimpleCookie simpleCookie = new SimpleCookie ("shiroSessionId");
        simpleCookie.setHttpOnly (true);// 减少XXS攻击
        simpleCookie.setPath ("/");
        simpleCookie.setMaxAge (IConstant.GLOBAL_COOKIE_TIMEOUT);//maxAge=-1表示浏览器关闭时失效此Cookie
        return simpleCookie;
    }

    @Bean
    public RedisSessionDAO sessionDAO () {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO ();
        redisSessionDAO.setRedisManager (redisManager ());
        redisSessionDAO.setSessionIdGenerator (sessionIdGenerator ());
        return redisSessionDAO;
    }

    @Bean
    public RedisManager redisManager () {
        RedisManager redisManager = new RedisManager ();
        redisManager.setHost (host + ":" + port);
        redisManager.setPassword (password);
        redisManager.setDatabase (database);
        return redisManager;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator () {
        return new RandomSessionIdGenerator ();
    }

    @Bean
    public UserRelam userRelam () {
        return new UserRelam ();
    }

    /**
     * 开启shiro aop注解支持; 使用代理方式,所以需要开启代码支持;
     *
     * @param securityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor (SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor ();
        authorizationAttributeSourceAdvisor.setSecurityManager (securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 授权所用配置
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator () {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator ();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass (true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor () {
        return new LifecycleBeanPostProcessor ();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean (SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean ();
        filterFactoryBean.setSecurityManager (securityManager);
        filterFactoryBean.setLoginUrl ("/");
        filterFactoryBean.setSuccessUrl ("/home");
        filterFactoryBean.setUnauthorizedUrl ("/login");
        // shiro拦截器
        Map<String, Filter> filters = new HashMap<> ();

        filterFactoryBean.setFilters (filters);

        // shiro资源拦截器,map<请求的URI地址,shiro内置拦截器>
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<> ();
        // 需要放行的资源
        filterChainDefinitionMap.put ("/css/**", "anon");
        filterChainDefinitionMap.put ("/js/**", "anon");
        filterChainDefinitionMap.put ("/plugin/**", "anon");
        filterChainDefinitionMap.put ("/fonts/**", "anon");
        filterChainDefinitionMap.put ("/images/**", "anon");
        filterChainDefinitionMap.put ("/druid/**", "anon");
        filterChainDefinitionMap.put ("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put ("/login", "anon");
        filterChainDefinitionMap.put ("/verifyCode", "anon");

        // 需要拦截的资源
        filterChainDefinitionMap.put ("logout", "logout");
        // user是登录过,如果开启了记住我是可以成功访问的，如果使用authc那么记住我就不能生效了
        filterChainDefinitionMap.put ("/**", "user");
        filterFactoryBean.setFilterChainDefinitionMap (filterChainDefinitionMap);
        return filterFactoryBean;
    }


}
