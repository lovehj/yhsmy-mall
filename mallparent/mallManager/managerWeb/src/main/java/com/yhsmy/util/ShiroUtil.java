package com.yhsmy.util;

import com.yhsmy.entity.vo.sys.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @auth 李正义
 * @date 2019/11/18 19:55
 **/
public class ShiroUtil {

    public static Subject getSubject () {
        return SecurityUtils.getSubject ();
    }

    /**
     * 获取session
     *
     * @return
     */
    public static Session getSession () {
        return ShiroUtil.getSubject ().getSession ();
    }

    /**
     * 获得登录用户的主体信息
     *
     * @return
     */
    public static User getUser () {
        return (User) ShiroUtil.getSubject ().getPrincipal ();
    }

}
