package com.soroosh.auth.notification.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements TransactionalMessagingAdapter {

    private final JavaMailSender sender;

    @Value("${mail-service.default-email}")
    private String defaultEmail;

    @Autowired
    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    protected void sendEmail(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        this.sender.send(message);
    }

    @Override
    public void sendMessage(String to, String title, String message) {
        this.sendEmail(this.defaultEmail, to, title, message);
    }
}
