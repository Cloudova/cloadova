package com.soroosh.auth.notification;

import com.soroosh.auth.config.identifierMatcher.IdentifierMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionalMessagingService {

    private final IdentifierMatcher matcher;

    @Autowired
    public TransactionalMessagingService(IdentifierMatcher matcher) {
        this.matcher = matcher;
    }

    public void sendTransactionalMessage(String to, String title, String message) {
        this.matcher.match(to).sendMessage(to, title, message);
    }
    public void sendVerification(String to, String code) {
        this.sendTransactionalMessage(to,"Account Verification","Your Verification Code is: " + code);
    }
}
