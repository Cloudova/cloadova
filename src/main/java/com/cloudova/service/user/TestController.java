package com.cloudova.service.user;

import com.cloudova.service.notification.TransactionalMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @Autowired
    private TransactionalMessagingService messagingService;

    @GetMapping("/test")
    public String test() {
        this.messagingService.sendTransactionalMessage("soroosh081@gmail.com", "Email Verification", "Your Verification Code is: 22054");
        this.messagingService.sendTransactionalMessage("+989378936669", "Email Verification", "Your Verification Code is: 22054");
        return "OK";
    }

}
