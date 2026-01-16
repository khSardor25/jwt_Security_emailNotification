package org.example.email_entity.service;

import lombok.AllArgsConstructor;

import org.example.email_entity.dto.EmailBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailServiceImpl {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendEmail(EmailBody email){

       try{
           SimpleMailMessage message = new SimpleMailMessage();
           message.setFrom(emailSender);
           message.setTo(email.getRecipient());
           message.setSubject(email.getSubject());
           message.setText(email.getBody());

           javaMailSender.send(message);
           return "Succesfully sent an email to " + email.getRecipient();
       }
       catch(Exception e){
           return "Failed to send an email to" + email.getRecipient();
       }


    }
}
