package com.soroosh.auth.commons;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Env {

    private final Environment environment;

    public Env(Environment environment) {
        this.environment = environment;
    }

    public boolean isTestingMode() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equals("testing")) {
                return true;
            }
        }
        return false;
    }
}
