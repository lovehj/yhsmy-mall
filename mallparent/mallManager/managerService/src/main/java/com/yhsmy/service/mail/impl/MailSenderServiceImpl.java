package com.yhsmy.service.mail.impl;

import com.yhsmy.entity.Json;
import com.yhsmy.service.mail.MailSenderServiceI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @auth 李正义
 * @date 2020/1/4 13:24
 **/
@Service
@Slf4j
public class MailSenderServiceImpl implements MailSenderServiceI {

    @Autowired
    private JavaMailSender mailSender;

    @Value ("${spring.mail.from}")
    private String from;


    @Override
    public Json sendTextMail (String to, String subject, String content, String... cc) {
        try{
            SimpleMailMessage message = new SimpleMailMessage ();
            message.setFrom (from);
            message.setTo (to);
            message.setSubject (subject);
            message.setText (content);
            if(cc != null && cc.length > 0) {
                message.setCc (cc);
            }
            mailSender.send (message);
        }catch (Exception e) {
            log.error ("发送文本邮件失败! ", e);
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    public Json sendHtmlMail (String to, String subject, String content, String... cc) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage ();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper (mimeMessage, true);
            mimeMessageHelper.setFrom (from);
            mimeMessageHelper.setTo (to);
            mimeMessageHelper.setSubject (subject);
            mimeMessageHelper.setText (content, true);
            if(cc != null && cc.length > 0) {
                mimeMessageHelper.setCc (cc);
            }
            mailSender.send (mimeMessage);
        }catch (Exception e){
            log.error ("发送HTML邮件失败!", e);
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    public Json sendAttchmentMail (String to, String subject, String content, List<String> filePaths, String... cc) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage ();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper (mimeMessage, true);
            mimeMessageHelper.setFrom (from);
            mimeMessageHelper.setTo (to);
            mimeMessageHelper.setSubject (subject);
            mimeMessageHelper.setText (content, true);
            if(cc != null && cc.length > 0) {
                mimeMessageHelper.setCc (cc);
            }
            if(!filePaths.isEmpty () && filePaths.size () > 0) {
                for(String filePath : filePaths) {
                    FileSystemResource resource = new FileSystemResource (new File (filePath));
                    mimeMessageHelper.addAttachment (resource.getFilename (), resource);
                }
            }
            mailSender.send (mimeMessage);
        }catch (Exception e){
            log.error ("发送带附件的邮件失败!", e);
            return Json.fail ();
        }
        return Json.ok ();
    }

    @Override
    public Json sendStaticResourceMail (String to, String subject, String content, String resourcePath, String resourceId, String... cc) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage ();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper (mimeMessage, true);
            mimeMessageHelper.setFrom (from);
            mimeMessageHelper.setTo (to);
            mimeMessageHelper.setSubject (subject);
            mimeMessageHelper.setText (content, true);
            if(cc != null && cc.length > 0) {
                mimeMessageHelper.setCc (cc);
            }
            if(StringUtils.isNotBlank (resourceId) && StringUtils.isNotBlank (resourcePath)) {
                mimeMessageHelper.addInline (resourceId, new FileSystemResource (new File (resourcePath)));
            }
            mailSender.send (mimeMessage);
        }catch (Exception e){
            log.error ("发送带静态资源的邮件失败!", e);
            return Json.fail ();
        }
        return Json.ok ();
    }
}
