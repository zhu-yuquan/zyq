package com.zyq.frechwind.pub.service;

import com.zyq.frechwind.base.YmlConfig;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
@Transactional
public class MessageSendService {
    /**
     * @param content 内容
     * @param email   接收人邮箱
     * @return
     * @throws MessagingException
     */
    public void sendSiteMessage(String subject, String content, String email) throws MessagingException {

        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // 设定mail server
        senderImpl.setHost(YmlConfig.value("spring.mail.host"));
        senderImpl.setUsername(YmlConfig.value("spring.mail.username"));
        senderImpl.setPassword(YmlConfig.value("spring.mail.password"));
        senderImpl.setDefaultEncoding(YmlConfig.value("spring.mail.default-encoding"));
//        senderImpl.setPort(25);

        MimeMessage mimeMessage = senderImpl.createMimeMessage();
        MimeMessageHelper mail = new MimeMessageHelper(mimeMessage);
        mail.setFrom(YmlConfig.value("spring.mail.from"));//电子邮件

        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(content, true);
        mail.setSentDate(new Date());

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        prop.setProperty("mail.transport.protocol", "smtp");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mimeMessage);

    }


}
