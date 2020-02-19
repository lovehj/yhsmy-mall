package com.yhsmy.web.wx;

import com.yhsmy.service.wx.ProcessServiceI;
import com.yhsmy.utils.WechatUtil;
import com.yhsmy.web.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @auth 李正义
 * @date 2020/2/12 22:17
 **/
@Api("微信认证接口")
@RestController
@Scope("request")
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    @Value("${wx.token}")
    private String token;

    @Autowired
    private ProcessServiceI processServiceI;

    /**
     * 微信认证
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return
     */
    @GetMapping("signature")
    public String signature (@RequestParam(value = "signature") String signature,
                             @RequestParam(value = "timestamp") String timestamp,
                             @RequestParam(value = "nonce") String nonce, @RequestParam(value = "echostr") String echostr) {
        return WechatUtil.checkSignature (token, signature, timestamp, nonce) ? echostr : null;
    }

    @PostMapping("signature")
    public String porcessMsg (HttpServletRequest request) throws IOException {
        return processServiceI.processRequest (request.getInputStream ());
    }

    @GetMapping("accessToken")
    public String accessToken () {
        return processServiceI.getAccessToken ();
    }
}
