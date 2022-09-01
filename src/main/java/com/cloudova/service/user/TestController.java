package com.cloudova.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @Autowired
    private JavaMailSender emailSender;

    @GetMapping("/test")
    public String test(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("info@soroosh-tanzadeh.ir");
        message.setTo("soroosh081@gmail.com");
        message.setSubject("test");
        message.setText("Hi Soroosh");

        emailSender.send(message);

        return "OK";
    }

}
