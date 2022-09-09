package com.cloudova.service.config.identifierMatcher;

import com.cloudova.service.notification.adapters.EmailService;
import com.cloudova.service.notification.adapters.SMSNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class TransactionalIdentifierConfig {

    private ApplicationContext appContext;

    @Autowired
    public TransactionalIdentifierConfig(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Bean
    public IdentifierMatcher getIdentifierMatcher() {
        return (new IdentifierMatcher(appContext))
                .put(SMSNotificationService.class, Pattern.compile("^\\+[1-9]\\d{10,14}$"))
                .put(EmailService.class, Pattern.compile("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$"));
    }
}
