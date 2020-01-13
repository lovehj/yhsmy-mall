package com.yhsmy.web.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yhsmy.IConstant;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.sys.Menu;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.MenuTypeEnum;
import com.yhsmy.service.sys.MenuServiceI;
import com.yhsmy.service.sys.MessageServiceI;
import com.yhsmy.service.sys.UserServcieI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.utils.VerifyCodeUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.HttpServletSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/9 23:23
 **/
@Api("登录注销接口")
@Controller
@Scope("request")
public class LoginController extends BaseController {

    @Autowired
    private UserServcieI userServcieI;

    @Autowired
    private MenuServiceI menuServiceI;

    @Autowired
    private MessageServiceI messageServiceI;

    @GetMapping(value = {"/", "login"})
    public String login () {
        return "login";
    }

    @ApiOperation(value = "登录接口", notes = "登录接口,密码采用SHA-1方式加密!", httpMethod = "POST", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录用户名,可以是用户名、邮箱、手机号", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "登录密码", dataType = "string"),
            @ApiImplicitParam(name = "rememberMe", value = "记住我，可选值为[on,off]", dataType = "string")
    })

    @PostMapping("/login")
    @ResponseBody
    public Json login (User user, String verifyCode, HttpServletRequest request) {
        if (StringUtils.isBlank (verifyCode)) {
            return Json.fail ("验证码必填!");
        }

        HttpSession session = request.getSession ();
        String verCode = (String) session.getAttribute ("verCode");
        if (verCode == null) {
            return Json.fail ("验证码已失效，请重新输入!");
        }

        if (!verCode.equalsIgnoreCase (verifyCode)) {
            return Json.fail ("验证码错误!");
        }

        LocalDateTime sessionCodeTime = (LocalDateTime) session.getAttribute ("codeTime");
        long past = sessionCodeTime.atZone (ZoneId.systemDefault ()).toInstant ().toEpochMilli ();
        long now = LocalDateTime.now ().atZone (ZoneId.systemDefault ()).toInstant ().toEpochMilli ();
        long sub = (now - past) / 60000; //分钟
        if (sub > 2) {
            return Json.fail ("验证码已过期，请重新获取!");
        }

        // 清除session里的验证码信息
        session.removeAttribute ("verCode");
        session.removeAttribute ("codeTime");

        String msgVal = "用户名或密码必填!";
        if (StringUtils.isEmpty (user.getUsername ())
                || StringUtils.isEmpty (user.getPassword ())) {
            return Json.fail (msgVal);
        }
        Subject subject = SecurityUtils.getSubject ();
        try {
            String username = user.getUsername (),
                    password = new SimpleHash (IConstant.SHIRO_SCCRITY,
                            username, user.getPassword (), IConstant.SHIRO_ITEAROTR).toString ();
            subject.login (new UsernamePasswordToken (username,
                    password, user.isRememberMe ()));
            if (subject.isAuthenticated ()) {
                return Json.ok ();
            }
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            msgVal = "用户名或密码错误!";
        } catch (ExcessiveAttemptsException e) {
            msgVal = "登录失败多次，账户被锁定!";
        }
        if (StringUtils.isNotEmpty (msgVal)) {
            return Json.fail (msgVal);
        }
        return Json.fail ("登录失败！");
    }

    @ApiOperation(value = "获取验证码")
    @GetMapping("verifyCode")
    public void verifyCode (HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader ("Pragma", "No-cache");
        response.setHeader ("Cache-Control", "no-cache");
        response.setDateHeader ("Expires", 0);
        response.setContentType ("image/jpeg");

        HttpSession session = request.getSession ();
        // 生成随机字串
        String verifyCode = VerifyCodeUtil.generateVerifyCode ();
        session.removeAttribute ("verCode");
        session.removeAttribute ("codeTime");
        session.setAttribute ("verCode", verifyCode.toLowerCase ());
        session.setAttribute ("codeTime", LocalDateTime.now ());

        // 生成图片
        int w = 104, h = 36;
        OutputStream out = response.getOutputStream ();
        VerifyCodeUtil.outputImage (w, h, out, verifyCode);
    }

    @GetMapping("home")
    public String home () {
        String viewName = "redirect:/login";
        Subject subject = ShiroUtil.getSubject ();
        if (subject.isAuthenticated () || subject.isRemembered ()) {
            Session session = ShiroUtil.getSession ();
            User user = ShiroUtil.getUser ();
            if (StringUtils.isEmpty (user.getRoleId ())) {
                return viewName;
            }
            session.setAttribute (IConstant.CURRENTPRINCIPAL, user);
            List<Menu> menuList = menuServiceI.getMenuListByRoleId (user.getRoleId (), MenuTypeEnum.OPERA.getKey (), true);
            JSONArray arr = JSONArray.parseArray (JSON.toJSONString (menuList));
            session.setAttribute ("menu", arr);
            session.setAttribute ("messageCount", messageServiceI.getListData (-1, 1, new QueryParams (), user).getCount ());
            viewName = "sys/home";
        }
        return viewName;
    }

    @ApiOperation(value = "退出接口", notes = "退出接口,会退出shiro登录!", httpMethod = "GET", response = String.class)
    @GetMapping("logout")
    public String logout () {
        Subject subject = SecurityUtils.getSubject ();
        if (subject.isAuthenticated () || subject.isRemembered ()) {
            subject.logout ();
        }
        return "redirect:/";
    }

}
