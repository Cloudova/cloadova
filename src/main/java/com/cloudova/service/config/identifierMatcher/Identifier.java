package com.cloudova.service.config.identifierMatcher;

import com.cloudova.service.notification.adapters.TransactionalMessagingAdapter;

import java.util.regex.Pattern;

public record Identifier(
        Class<? extends TransactionalMessagingAdapter> service,
        Pattern pattern
) {
}
