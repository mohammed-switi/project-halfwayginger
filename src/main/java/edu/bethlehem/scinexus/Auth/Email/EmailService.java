package edu.bethlehem.scinexus.Auth.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);


    private JavaMailSender mailSender;
    @Override
    @Async
    public void send(String to, String email) {
            try {
                MimeMessage mimeMessage=mailSender.createMimeMessage();
                MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,"utf-8");
                helper.setText(email,true);
                helper.setTo(to);
                helper.setSubject("Confirm your Email");
                helper.setFrom("admin@scinexus.com");
                mailSender.send(mimeMessage);

            }catch (MessagingException e){
                throw  new EmailException("failed to send email", HttpStatus.BAD_REQUEST,e.getCause());
            }
    }
}
