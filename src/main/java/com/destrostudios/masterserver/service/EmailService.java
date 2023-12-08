package com.destrostudios.masterserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("support@destrostudios.com");
        message.setTo(to);
        message.setSubject("destrostudios - " + subject);
        message.setText(text);
        mailSender.send(message);
    }
}
