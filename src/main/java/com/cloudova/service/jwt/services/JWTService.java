package com.cloudova.service.jwt.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloudova.service.jwt.models.AuthenticationToken;
import com.cloudova.service.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${app}")
    private String appName;

    private final AuthenticationTokenService accessTokenService;

    @Autowired
    public JWTService(AuthenticationTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Bean
    public Algorithm getAlgorithm() {
        return Algorithm.HMAC512(this.secret);
    }

    public DecodedJWT validateJWT(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(this.appName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        this.accessTokenService.findById(Long.parseUnsignedLong(decodedJWT.getId())).orElseThrow(() -> new JWTVerificationException("Token ID is invalid"));
        return decodedJWT;
    }

    public void invokeToken(String token){
        Algorithm algorithmHS = Algorithm.HMAC512(this.secret);
        JWTVerifier verifier = JWT.require(algorithmHS)
                .withIssuer(this.appName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        AuthenticationToken accessToken = this.accessTokenService.findById(Long.parseUnsignedLong(decodedJWT.getId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        this.accessTokenService.deleteAuthenticationToken(accessToken);
    }

    public String generateToken(User userDetails) {
        AuthenticationToken refreshToken = this.accessTokenService.createAuthenticationToken(userDetails);
        System.out.println("Values: " + userDetails.toHashMap().values());
        return JWT.create()
                .withIssuer(this.appName)
                .withSubject(String.valueOf(userDetails.getId()))
                .withIssuedAt(new Date())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(AuthenticationTokenService.tokenExpirationDays)))
                .withClaim("refreshToken", refreshToken.getRefreshToken())
                .withClaim("preferred_username", userDetails.getUsername())
                .withClaim("name", userDetails.getName())
                .withClaim("user", userDetails.toHashMap())
                .withJWTId(String.valueOf(refreshToken.getId()))
                .sign(getAlgorithm());
    }
}
