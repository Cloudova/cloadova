package com.soroosh.auth.config.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.soroosh.auth.jwt.services.JWTService;
import com.soroosh.auth.user.models.User;
import com.soroosh.auth.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public AuthenticationFilter(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken token = createToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(token);

        } catch (com.auth0.jwt.exceptions.JWTDecodeException ex) {
            // Nothing
        }
        filterChain.doFilter(request, response);
    }

    private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
        return authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ");
    }

    private UsernamePasswordAuthenticationToken createToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        DecodedJWT decodedJWT = this.jwtService.validateJWT(token);
        String username = decodedJWT.getClaim("preferred_username").asString();
        User user = this.userService.findByUsername(username);
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

}
