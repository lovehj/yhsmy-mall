package com.yhsmy.config.rabbitmq;

import com.yhsmy.config.RabbitMqConfig;
import com.yhsmy.entity.RabbitMqMessage;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.RabbitMqOperaTypeEnum;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.service.mail.MailSenderServiceI;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * rabbitMq的所有消费者类
 *
 * @auth 李正义
 * @date 2020/1/3 21:36
 **/
@Component
public class RabbitConsumer {

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private MailSenderServiceI mailSenderServiceI;

    @RabbitListener(queuesToDeclare = @Queue(RabbitMqConfig.SIMPLE_QUEUE))
    public void simpleListener (RabbitMqMessage rabbitMqMessage) {
        if (rabbitMqMessage.getUser () == null || !(rabbitMqMessage.getUser () instanceof User)) {
            return;
        }

        // 更新数据库
        if (rabbitMqMessage.isOperaDb () && rabbitMqMessage.getDbFieldsMap ().size () > 0
                && rabbitMqMessage.getDbWhereMap ().size () > 0) {
            UpdateMap updateMap = new UpdateMap (rabbitMqMessage.getDbTableName ());
            for (Map.Entry<String, String> entry : rabbitMqMessage.getDbFieldsMap ().entrySet ()) {
                updateMap.addField (entry.getKey (), entry.getValue ());
            }

            for (Map.Entry<String, String> entry : rabbitMqMessage.getDbWhereMap ().entrySet ()) {
                updateMap.addWhere (entry.getKey (), entry.getValue ());
            }

            if (rabbitMqMessage.getOperaSqlType () == 0) {
                this.mybatisMapper.update (updateMap);
            } else {
                this.mybatisMapper.delete (updateMap);
            }
        }

        if(!rabbitMqMessage.isSendMessage ()) {
            return;
        }

        User user = (User) rabbitMqMessage.getUser ();
        if (rabbitMqMessage.getOperaCtype () == RabbitMqOperaTypeEnum.EMAIL.getKey ()) {
            // 发送邮件的业务逻辑
            mailSenderServiceI.sendTextMail (user.getEmail (), rabbitMqMessage.getSubTitle (), rabbitMqMessage.getContent (), null);
        } else if (rabbitMqMessage.getOperaCtype () == RabbitMqOperaTypeEnum.SMS.getKey ()) {
            // 发送短信的业务逻辑
        } else if (rabbitMqMessage.getOperaCtype () == RabbitMqOperaTypeEnum.EMAILANDSMS.getKey ()) {
            //发送邮件和短信的业务逻辑
        } else {
            // 发送的其它业务逻辑
        }
    }

}
