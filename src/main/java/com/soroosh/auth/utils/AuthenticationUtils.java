package com.soroosh.auth.utils;

import com.soroosh.auth.user.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static User getCurrentUser() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
       return (User) token.getPrincipal();
    }
}
