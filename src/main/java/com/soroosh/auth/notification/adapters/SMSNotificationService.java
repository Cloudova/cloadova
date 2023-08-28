package com.soroosh.auth.notification.adapters;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class SMSNotificationService implements TransactionalMessagingAdapter {
    @Override
    public void sendMessage(String to, String title, String message) {
        throw new NotImplementedException();
    }
}
