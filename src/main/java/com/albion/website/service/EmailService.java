package com.albion.website.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String recipientAddress, String subject, String message, String url) {
        sendEmail(recipientAddress, subject, String.format("%s %s", message, url));
    }

    @Async
    public void sendEmail(String recipientAddress, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setFrom("info@thmirk.com");
        email.setSubject(subject);
        email.setText(message);
        try {
            mailSender.send(email);
        } catch (MailException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}