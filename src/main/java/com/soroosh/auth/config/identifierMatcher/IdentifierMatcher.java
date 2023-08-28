package com.soroosh.auth.config.identifierMatcher;

import com.soroosh.auth.notification.adapters.TransactionalMessagingAdapter;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IdentifierMatcher {
    private final ApplicationContext appContext;
    private final List<Identifier> patterns;

    public IdentifierMatcher(ApplicationContext appContext) {
        this.appContext = appContext;
        patterns = new ArrayList<>();
    }

    public IdentifierMatcher put(Class<? extends TransactionalMessagingAdapter> key, Pattern value) {
        patterns.add(new Identifier(key, value));
        return this;
    }

    public TransactionalMessagingAdapter match(String value) {
        return this.appContext.getBean(this.patterns.stream()
                .filter((identifier -> identifier.pattern().matcher(value).matches()))
                .findFirst().orElseThrow(() -> new InvalidIdentifier("Invalid Identifier"))
                .service());
    }

}
