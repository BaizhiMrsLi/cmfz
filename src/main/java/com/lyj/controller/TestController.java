package com.lyj.controller;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@RestController
@RequestMapping("test")
public class TestController {
    @Resource
    JmsTemplate jmsTemplate;

    @RequestMapping("test")
    public void ProductSend(String msg) {
        jmsTemplate.convertAndSend("SpringbootQUEUE", msg);
    }

    @JmsListener(destination = "SpringbootQUEUE")
    public void Consumers(TextMessage msg) {
        try {
            String text = msg.getText();
            System.out.println(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
