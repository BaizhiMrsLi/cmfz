package com.lyj.cmfz.test;

import com.lyj.service.MailServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

public class QQEmailTest extends Basetest {

    @Autowired
    private MailServiceImpl mailService;

    @Test
    public void test01() throws MessagingException {
        mailService.sendHtmlMail("875440649@qq.com", "主题", "你好啊！");
    }
}
