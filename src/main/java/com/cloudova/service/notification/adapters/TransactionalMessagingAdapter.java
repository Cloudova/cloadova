package com.cloudova.service.notification.adapters;


public interface TransactionalMessagingAdapter {
    void sendMessage(String to, String title, String message);
}
