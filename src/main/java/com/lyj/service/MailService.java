package com.lyj.service;

public interface MailService {
    //发送普通邮件
    void sendHtmlMail(String address, String title, String content);

    //发送带附件邮件
    void sendAttachMail(String address, String title, String content,
                        String filePath, String fileName);
}
