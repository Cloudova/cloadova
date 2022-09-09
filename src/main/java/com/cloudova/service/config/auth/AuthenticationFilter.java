package com.cloudova.service.config.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloudova.service.jwt.services.JWTService;
import com.cloudova.service.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter
@Component
public class AuthenticationFilter implements Filter {

    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public AuthenticationFilter(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getHeader("Authorization") != null) {
            try {
                String jwtToken = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
                System.out.println(jwtToken);
                DecodedJWT decodedJWT = this.jwtService.validateJWT(jwtToken);
                String username = decodedJWT.getClaim("preferred_username").asString();
                UserDetails userDetails = this.userService.loadUserByUsername(username);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, jwtToken));
            } catch (Exception ex) {
                // Nothing
            }
        }
        filterChain.doFilter(request, response);
    }
}
