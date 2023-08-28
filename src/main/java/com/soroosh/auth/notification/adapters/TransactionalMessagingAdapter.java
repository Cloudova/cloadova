package com.soroosh.auth.notification.adapters;


public interface TransactionalMessagingAdapter {
    void sendMessage(String to, String title, String message);
}
