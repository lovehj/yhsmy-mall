package com.yhsmy.config.rabbitmq;

import com.yhsmy.config.RabbitMqConfig;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.RabbitMqMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbitMq的生产者
 *
 * @auth 李正义
 * @date 2020/1/3 21:01
 **/
@Component
public class RabbitProducer {

    @Autowired
    private AmqpTemplate rebbitmqTemplate;

    /**
     * 简单模式
     *
     * @param rabbitMqMessage
     * @return
     */
    public Json simpleProducer (RabbitMqMessage rabbitMqMessage) {
        if (rabbitMqMessage == null) {
            return Json.fail ();
        }
        this.rebbitmqTemplate.convertAndSend (RabbitMqConfig.SIMPLE_QUEUE, rabbitMqMessage);
        return Json.ok ();
    }

    public Json topicProducer(RabbitMqMessage rabbitMqMessage) {
        if (rabbitMqMessage == null) {
            return Json.fail ();
        }
        this.rebbitmqTemplate.convertAndSend (RabbitMqConfig.TOPIC_EXCHANGE, "路由规则", rabbitMqMessage);
        return Json.ok ();
    }




}


