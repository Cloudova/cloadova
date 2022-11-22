package com.cloudova.service.jwt.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloudova.service.jwt.models.AuthenticationToken;
import com.cloudova.service.project.models.ApplicationUser;
import com.cloudova.service.user.models.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.util.Date;

@Service
public class JWTService {

    private final AuthenticationTokenService accessTokenService;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${app}")
    private String appName;

    private final RSAKeyService rSAKeyService;

    @Autowired
    public JWTService(AuthenticationTokenService accessTokenService, RSAKeyService rSAKeyService) {
        this.accessTokenService = accessTokenService;
        this.rSAKeyService = rSAKeyService;
    }

    @SneakyThrows
    @Bean
    public Algorithm getAlgorithm() {
        KeyPair keyPair = this.rSAKeyService.getKeyPair();
        return Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(),(RSAPrivateKey) keyPair.getPrivate());
    }

    public Algorithm verificationAlgorithm(){
        return Algorithm.RSA256((RSAPublicKey)  this.rSAKeyService.getPublicKey(),null);
    }

    public DecodedJWT validateJWT(String token) {
        JWTVerifier verifier = JWT.require(verificationAlgorithm())
                .withIssuer(this.appName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        this.accessTokenService.findById(Long.parseUnsignedLong(decodedJWT.getId())).orElseThrow(() -> new JWTVerificationException("Token ID is invalid"));
        return decodedJWT;
    }

    public void getPublicKeys(){

    }

    public void invokeToken(String token) {
        JWTVerifier verifier = JWT.require(verificationAlgorithm())
                .withIssuer(this.appName)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        AuthenticationToken accessToken = this.accessTokenService.findById(Long.parseUnsignedLong(decodedJWT.getId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        this.accessTokenService.deleteAuthenticationToken(accessToken);
    }

    public String generateToken(User userDetails) {
        AuthenticationToken refreshToken = this.accessTokenService.createAuthenticationToken(userDetails);
        return JWT.create()
                .withIssuer(this.appName)
                .withSubject(String.valueOf(userDetails.getId()))
                .withIssuedAt(new Date())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(AuthenticationTokenService.tokenExpirationDays)))
                .withClaim("refreshToken", refreshToken.getRefreshToken())
                .withClaim("preferred_username", userDetails.getUsername())
                .withClaim("name", userDetails.getName())
                .withClaim("user", userDetails.toHashMap())
                .withClaim("type", JWTTokenTypes.DEVELOPER_TOKEN.name())
                .withJWTId(String.valueOf(refreshToken.getId()))
                .sign(getAlgorithm());
    }

    public String generateTokenForApplicationUser(ApplicationUser userDetails) {
        AuthenticationToken refreshToken = this.accessTokenService.createAuthenticationToken(userDetails);
        return JWT.create()
                .withIssuer(this.appName)
                .withSubject(String.valueOf(userDetails.getId()))
                .withIssuedAt(new Date())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(AuthenticationTokenService.tokenExpirationDays)))
                .withClaim("refreshToken", refreshToken.getRefreshToken())
                .withClaim("preferred_username", userDetails.getEmail())
                .withClaim("name", userDetails.getName())
                .withClaim("user", userDetails.toHashMap())
                .withClaim("type", JWTTokenTypes.APPLICATION_TOKEN.name())
                .withClaim("application", userDetails.getApplication().getSubdomain())
                .withJWTId(String.valueOf(refreshToken.getId()))
                .sign(getAlgorithm());
    }
}
