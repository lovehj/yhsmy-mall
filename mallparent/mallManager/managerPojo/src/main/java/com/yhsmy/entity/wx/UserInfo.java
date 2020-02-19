package com.yhsmy.entity.wx;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 微信公众号用户详细信息
 *
 * @auth 李正义
 * @date 2020/2/15 14:15
 **/
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -113066167450189383L;

    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号
     */
    private int subscribe;

    /**
     * 用户的标识，对当前公众号唯一
     */
    private String openid;

    /**
     * 用户的昵称
     */
    private String nickname;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private int sex;

    /**
     * 用户所在城市
     */
    private String city;

    /**
     * 用户所在国家
     */
    private String country;

    /**
     * 用户所在省份
     */
    private String province;

    /**
     * 用户的语言，简体中文为zh_CN
     */
    private String language;

    /**
     * 用户头像
     */
    private String headimgurl;

    /**
     * 用户关注时间，为时间戳
     */
    private long subscribe_time;

    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
    private String unionid;

    /**
     * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
     */
    private String remark;

    /**
     * 用户所在的分组ID
     */
    private String groupid;

    /**用户被打上的标签ID列表
     *
     */
    private String tagid_list;

    /**
     * 返回用户关注的渠道来源
     */
    private String subscribe_scene;

    /**
     * 二维码扫码场景（开发者自定义）
     */
    private String qr_scene;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    private String qr_scene_str;

    private int state;

    private String creator;

    private LocalDateTime createTime;

    private String modifyor;

    private LocalDateTime modifyTime;


}
