package com.lzhch.practice.messagequeue.pulsar.consumer;

import com.lzhch.practice.messagequeue.pulsar.config.PulsarProperties;
import com.lzhch.practice.messagequeue.pulsar.topic.PulsarTopic;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 消费者
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/10/13 14:33
 */

@Slf4j
@Component
public class PulsarConsumer {

    @Resource
    private PulsarClient pulsarClient;
    @Resource
    private PulsarProperties pulsarProperties;

    /**
     * 初始化消费者
     * messageListener :
     * 当接收到一个新的消息，就会回调 MessageListener的receive方法。
     * 消息将会保证按顺序投放到单个消费者的同一个线程，因此可以保证顺序消费
     * 除非应用程序或broker崩溃，否则只会为每条消息调用此方法一次
     * 应用程序负责调用消费者的确认方法来确认消息已经被消费
     * 应用程序负责处理消费消息时可能出现的异常
     * <p>
     * 消费逻辑:
     * 生产者发送消息, 同一个 topic 下的所有的订阅者都可以接受到消息进行消费(广播,多个订阅者时可能需要进行幂等处理)
     * 每个订阅者下面可以有多个消费者, 通过 subscriptionType 控制消费者的类别
     * 如果是3个订阅者, 且每个订阅者下面有3个消费者, 且是独占模式的话:
     * 每个订阅者下面都有且仅有一个消费者可以进行消费, 一共有 3 个消费者可以消费到消息, 要考虑是否要对接口做幂等处理
     *
     * <p>
     * Author: lzhch 2023/8/23 16:27
     * Since: 1.0.0
     */
    @PostConstruct
    public void initConsumer() throws PulsarClientException {
        pulsarClient.newConsumer(Schema.STRING)
                .topic(PulsarTopic.PULSAR_TOPIC)
                // 订阅名称.    同一个 topic 下如果有多个订阅者(消费者组), 每个订阅者都会接收消息(相当于广播模式);
                // 每个订阅者可以有多个消费者, 通过 subscriptionType 控制消费者的类别
                .subscriptionName("排产订单导入")
                // 应答超时时间, 时间范围内没有应答会重新投递消息
                // .ackTimeout(30, TimeUnit.SECONDS)
                // 共享模式, 针对于 subscriptionName 属性来说, 并不是针对于 topic 属性来说的
                .subscriptionType(SubscriptionType.Shared)
                // 重试x次失败则放入死信队列
                .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(pulsarProperties.getMaxRedeliverCount()).build())
                // 消费失败后多久重新发送
                .negativeAckRedeliveryDelay(60, TimeUnit.SECONDS)
                .messageListener((MessageListener<String>) (consumer, msg) -> {
                    log.info("pulsar 接收到消息:{}", msg.getValue());
                    try {
                        consumer.acknowledge(msg);
                    } catch (PulsarClientException e) {
                        log.error("pulsar 消费失败 :", e);
                        // 确认消息消费失败
                        consumer.negativeAcknowledge(msg);
                    }
                })
                .subscribe();
    }

}
