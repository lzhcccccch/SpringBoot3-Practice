package com.lzhch.practice.messagequeue.pulsar.controller;

import com.lzhch.practice.messagequeue.pulsar.provider.PulsarProvider;
import jakarta.annotation.Resource;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2023/10/13 14:42
 */

@RestController
@RequestMapping(value = "pulsar")
public class PulsarController {

    @Resource
    private PulsarProvider pulsarProvider;

    @PostMapping(value = "sendMsg")
    public void sendMsg(String msg) throws PulsarClientException {
        this.pulsarProvider.sendMessage(msg);
    }

}
