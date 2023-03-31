package com.mindhub.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    private final String from="support@epidata.com";

    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Mail sent successfully");
    }

    public void sendEmailHtml(String toEmail,String subject, String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            message.setContent(content,"text/html");
            mailSender.send(message);
            System.out.println("Mail sent successfully");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void sendEmailAttachment(String toEmail,String subject){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("support@epidata.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText("Hi, you have a new file");
            FileSystemResource file
                    = new FileSystemResource(new File("./src/main/resources/static/ejemplo.txt"));
            helper.addAttachment("show.txt", file);
            mailSender.send(message);
            System.out.println("Mail sent successfully");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
