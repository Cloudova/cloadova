package com.soroosh.auth.config.identifierMatcher;

import com.soroosh.auth.notification.adapters.TransactionalMessagingAdapter;

import java.util.regex.Pattern;

public record Identifier(
        Class<? extends TransactionalMessagingAdapter> service,
        Pattern pattern
) {
}
