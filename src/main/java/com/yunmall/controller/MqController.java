package com.yunmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

@Controller
public class MqController {
    @Autowired
        private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;
    @RequestMapping("/queue/test")
    public String sendQueue(@RequestParam String str) {
        this.sendMessage(this.queue, str);
        return "index";
    }

    @RequestMapping("/topic/test")
    public String sendTopic(@RequestParam String str) {
        this.sendMessage(this.topic, str);
        return "index";
    }

    // 发送消息，destination是发送到的队列，message是待发送的消息
    private void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
}