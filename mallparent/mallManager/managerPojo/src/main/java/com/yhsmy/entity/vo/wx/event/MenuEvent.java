package com.yhsmy.entity.vo.wx.event;

/**
 * 自定义菜单事件，分为点击和跳转2种
 *
 * @auth 李正义
 * @date 2020/2/13 11:24
 **/
public class MenuEvent extends BaseEvent {

    private static final long serialVersionUID = -8782317974815899575L;
    /**
     * 点击：事件KEY值，与自定义菜单接口中KEY值对应
     * 跳转：事件KEY值，设置的跳转URL
     */
    private String EventKey;
}
