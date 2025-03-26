package com.sec.service;

import com.sec.entity.User;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;

    private JavaMailSender javaMailSender;
    private UserServiceImpl userServiceImpl;

    public EmailService(JavaMailSender javaMailSender, UserServiceImpl userServiceImpl) {
        this.javaMailSender = javaMailSender;
        this.userServiceImpl = userServiceImpl;
    }

    public void sendMessage(String email, String activationCode) {

        SimpleMailMessage message;
        try {
            message = new SimpleMailMessage();
            message.setFrom(MESSAGE_FROM);
            message.setTo(email);
            message.setSubject("Sikeres regisztráció");
            message.setText("Kedves "  + email + "! \n  Köszönjük, hogy regisztráltál! \n" + "az aktiváló kódód: " + activationCode);

        } catch (Exception e) {
            log.error("Hiba a küldéskor az alábbi címre: " + email + " " + e);
        }
    }
}
