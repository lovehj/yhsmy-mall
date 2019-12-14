package com.yhsmy.config.shiro.realm;

import com.yhsmy.IConstant;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.service.sys.MenuServiceI;
import com.yhsmy.service.sys.UserServcieI;
import com.yhsmy.util.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @auth 李正义
 * @date 2019/11/11 23:07
 **/
public class UserRelam extends AuthorizingRealm {

    @Autowired
    private UserServcieI userServcieI;

    @Autowired
    private MenuServiceI menuServiceI;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo (PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo ();
        User user = (User) principalCollection.getPrimaryPrincipal ();
        String roleId = user.getRoleId ();
        if (StringUtils.isNotEmpty (roleId)) {
            List<Menu> menuList = menuServiceI.getMenuListByRoleId (roleId, -1,false);
            Set<String> premissions = menuList.stream ().filter (m -> StringUtils.isNotEmpty (StringUtils.trim (m.getPermission ()))).map (m -> m.getPermission ()).collect (Collectors.toSet ());
            info.addStringPermissions (premissions);
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo (AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername ();
        int loginType = 0;
        //0=用户名 1=手机 2=邮箱 3=微信公众号的 openId
        if (IConstant.MOBILE_PATTERN.matcher (username).find ()) {
            loginType = 1;
        } else if (IConstant.EMAIL_PATTERN.matcher (username).find ()) {
            loginType = 2;
        } else if (username.length () > 10) {
            loginType = 3;
        }
        User user = userServcieI.getUserByLogin (loginType, username);
        if (user == null || StringUtils.isEmpty (user.getRoleId ())) {
            throw new UnknownAccountException ();
        }

        user.setLoginType (loginType);
        return new SimpleAuthenticationInfo (user, user.getPassword (), getName ());
    }
}
