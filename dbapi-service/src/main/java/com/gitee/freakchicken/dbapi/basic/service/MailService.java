package com.gitee.freakchicken.dbapi.basic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Async
    public void sendSimpleMail(String[] to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setFrom(username);
            message.setTo(to);
            message.setSentDate(new Date());
            message.setText(content);
            javaMailSender.send(message);
            log.info("send mail success");
        } catch (Exception e) {
            log.error("send mail failed");
            log.error(e.getMessage(), e);
        }

    }

}
