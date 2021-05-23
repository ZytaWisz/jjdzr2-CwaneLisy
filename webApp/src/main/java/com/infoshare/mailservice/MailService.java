package com.infoshare.mailservice;


import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    private static final String EMAIL_SENDER = "Helpick! <helpick.application@gmail.com>";
    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String subject, String content, String... recipient){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setBcc(recipient);
        simpleMailMessage.setFrom(EMAIL_SENDER);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        log.info("Sending message to "+ Arrays.stream(recipient).collect(Collectors.joining(",")));
        javaMailSender.send(simpleMailMessage);
    }
}
