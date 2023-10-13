package com.lzhch.practice.messagequeue.pulsar.config;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.impl.auth.oauth2.AuthenticationFactoryOAuth2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URL;

/**
 * pulsar 配置
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/10/13 15:05
 */

@Configuration
@EnableConfigurationProperties({PulsarProperties.class})
public class PulsarConfig {

    @Value("${spring.profiles.active}")
    String profileActive;

    @Bean
    public PulsarClient createPulsarClient(PulsarProperties pulsarProperties) throws IOException {
        return oauth2PulsarClient(pulsarProperties);
    }

    private PulsarClient oauth2PulsarClient(PulsarProperties pulsarProperties) throws IOException {
        URL issuerUrl = new URL(pulsarProperties.getIssuerUrl());
        String configFile = "credentials_file_" + profileActive + ".json";
        URL credentialsUrl = this.getClass().getClassLoader().getResource(configFile);
        Assert.notNull(credentialsUrl, "获取配置文件路径失败");
        String audience = "";
        return PulsarClient.builder()
                .serviceUrl(pulsarProperties.getClusterUrl())
                .authentication(AuthenticationFactoryOAuth2.clientCredentials(issuerUrl, credentialsUrl, audience))
                .build();
    }

}
