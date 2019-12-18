package com.lyj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendHtmlMail(String address, String title, String content) {
        /*MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(address);
            helper.setSubject(title);
            helper.setText(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        javaMailSender.send(message);*/
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 邮件发送者
        message.setTo(address); // 邮件接受者
        message.setSubject(title); // 主题
        message.setText(content); // 内容

        javaMailSender.send(message);

    }

    @Override
    public void sendAttachMail(String address, String title, String content, String filePath, String fileName) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(address);
            helper.setSubject(title);
            helper.setText(content, true);
            File file = new File(filePath);
            FileSystemResource res = new FileSystemResource(file);
            //fileName  -----   txt/xml/...
            helper.addInline(fileName, res);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        javaMailSender.send(message);

    }
}
