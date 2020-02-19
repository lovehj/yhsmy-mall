package com.yhsmy.service.mail;

import com.yhsmy.entity.Json;

import java.util.List;

/**
 * @auth 李正义
 * @date 2020/1/4 13:10
 **/
public interface MailSenderServiceI {

    /**
     * 发送简单文本邮件
     *
     * @param to      接收人
     * @param subject 邮件主题
     * @param content 邮件正文
     * @param cc      抄送到其它用户 null=忽略
     */
    public Json sendTextMail (String to, String subject, String content, String... cc);

    /**
     * 发送HTML邮件带抄送人
     *
     * @param to      接收人
     * @param subject 邮件主题
     * @param content 邮件正文
     * @param cc      抄送到其它用户 null=忽略
     */
    public Json sendHtmlMail (String to, String subject, String content, String... cc);

    /**
     * 发送还附件的邮件
     *
     * @param to        接收人
     * @param subject   邮件主题
     * @param content   邮件正文
     * @param filePaths 附件地址集合
     * @param cc        抄送到其它用户 null=忽略
     */
    public Json sendAttchmentMail (String to, String subject, String content, List<String> filePaths, String... cc);

    /**
     * 发送正文中有静态资源的文件
     *
     * @param to           接收人
     * @param subject      邮件主题
     * @param content      邮件正文
     * @param resourcePath 静态资源路径
     * @param resourceId   静态资源ID
     * @param cc           抄送到其它用户 null=忽略
     */
    public Json sendStaticResourceMail (String to, String subject, String content, String resourcePath, String resourceId, String... cc);

}
