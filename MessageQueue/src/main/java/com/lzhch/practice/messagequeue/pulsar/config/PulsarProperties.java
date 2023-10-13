package com.lzhch.practice.messagequeue.pulsar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * pulsar 属性
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/10/13 15:10
 */

@Data
@ConfigurationProperties(prefix = PulsarProperties.PREFIX)
public class PulsarProperties {

    public static final String PREFIX = "pulsar";

    /**
     * OAuth2认证服务器地址
     */
    private String issuerUrl;

    /**
     * pulsar集群访问地址
     */
    private String clusterUrl;

    /**
     * topic
     */
    private String topic;

    /**
     * iHaier2.0 消息推送的
     */
    private String webhookUrl;

    /**
     * 消息在被发送到死信队列之前将被重新传递的最大次数
     */
    private Integer maxRedeliverCount;

}
