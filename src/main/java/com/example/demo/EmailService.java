package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(
            String to,
            String subject,
            String text) {

        System.out.println("Sending email to: [" + to + "]");

        if (to == null || to.trim().isEmpty()) {
            throw new RuntimeException("Recipient email address is empty");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("librarymgmt09@gmail.com");
        message.setTo(to.trim());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}