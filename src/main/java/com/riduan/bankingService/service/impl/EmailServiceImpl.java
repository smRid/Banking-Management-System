package com.riduan.bankingService.service.impl;

import com.riduan.bankingService.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailDetails(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipients());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
