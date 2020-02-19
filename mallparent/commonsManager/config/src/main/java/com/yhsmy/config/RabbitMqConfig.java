package com.yhsmy.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitMq配置,配置关系
 *
 * @auth 李正义
 * @date 2019/12/28 19:21
 **/
@Configuration
public class RabbitMqConfig {

    // 简单模式
    public static final String SIMPLE_QUEUE = "smy_simple_worker_queue";

    // topic 模式
    public static final String TOPIC_QUEUE_1 = "smy_topic_queue_1";
    public static final String TOPIC_QUEUE_2 = "smy_topic_queue_2";
    public static final String TOPIC_EXCHANGE = "smy_topic_exchange";
    public static final String TOPIC_BIND_1 = "com.yhsmy.1";
    public static final String TOPIC_BIND_2 = "com.yhsmy.2";

    //fanout 模式
    public static final String FANOUT_QUEUE_1 = "smy_fanout_queue_1";
    public static final String FANOUT_QUEUE_2 = "smy_fanout_queue_2";
    public static final String FANOUT_EXCHANGE = "smy_fanout_exchange";

    //direct 模式
    public static final String DIRECT_QUEUE_1 = "smy_direct_queue_1";
    public static final String DIRECT_QUEUE_2 = "smy_direct_queue_2";
    public static final String DIRECT_EXCHANGE = "smy_direct_exchange";
    public static final String DIRECT_BINDING = "com.smy.*";

    /**
     * direct 主题匹配模型
     *
     * @return
     */
    @Bean
    public Queue topicQueue1 () {
        return new Queue (TOPIC_QUEUE_1);
    }

    @Bean
    public Queue topicQueue2 () {
        return new Queue (TOPIC_QUEUE_2);
    }

    @Bean
    public TopicExchange topicExchange () {
        return new TopicExchange (TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBind1 () {
        return BindingBuilder.bind (topicQueue1 ()).to (topicExchange ()).with (TOPIC_BIND_1);
    }

    @Bean
    public Binding topicBind2 () {
        return BindingBuilder.bind (topicQueue2 ()).to (topicExchange ()).with (TOPIC_BIND_2);
    }

    /**
     * fanout 发布/订阅模型,绑定了这个交换器的所有队列都收到消息
     *
     * @return
     */

    @Bean
    public Queue fanoutQueue1 () {
        return new Queue (FANOUT_QUEUE_1);
    }

    @Bean
    public Queue fanoutQueue2 () {
        return new Queue (FANOUT_QUEUE_2);
    }

    @Bean
    public FanoutExchange fanoutExchange () {
        return new FanoutExchange (FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBind1 () {
        return BindingBuilder.bind (fanoutQueue1 ()).to (fanoutExchange ());
    }

    @Bean
    public Binding fanoutBind2 () {
        return BindingBuilder.bind (fanoutQueue2 ()).to (fanoutExchange ());
    }

    /**
     * 如果消息中的路由键与binding中的binding key一致，交换器将消息发送到对应的队列中，路由键与队列名完全一致。
     *
     * @return
     */

    @Bean
    public Queue directQueue1 () {
        return new Queue (DIRECT_QUEUE_1);
    }

    @Bean
    public DirectExchange directExchange () {
        return new DirectExchange (DIRECT_EXCHANGE);
    }

    @Bean
    public Binding directBind1 () {
        return BindingBuilder.bind (directQueue1 ()).to (directExchange ()).with (DIRECT_BINDING);
    }


}
