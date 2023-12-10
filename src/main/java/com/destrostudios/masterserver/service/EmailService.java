package com.destrostudios.masterserver.service;

import com.destrostudios.masterserver.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("support@destrostudios.com");
        message.setTo(email.getTo());
        message.setSubject("destrostudios - " + email.getSubject());
        message.setText(email.getText());
        mailSender.send(message);
    }
}
