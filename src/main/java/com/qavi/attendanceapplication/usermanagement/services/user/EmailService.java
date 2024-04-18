package com.qavi.attendanceapplication.usermanagement.services.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String toEmail, String subject, String Message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(Message);
        mailMessage.setFrom("gate68dev@gmail.com");
        javaMailSender.send(mailMessage);
    }

    public void serviceDueEmail(String toEmail, String subject, String Message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(Message);
        javaMailSender.send(mailMessage);
    }

    public void invoiceDueEmail(String toEmail, String subject, String Message, String business_profile) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(Message);
        javaMailSender.send(mailMessage);
    }
}
