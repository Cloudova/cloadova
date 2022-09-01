package com.cloudova.service.jwt.services;

import com.cloudova.service.jwt.models.AuthenticationToken;
import com.cloudova.service.jwt.models.AuthenticationTokenRepository;
import com.cloudova.service.user.models.User;
import com.cloudova.service.user.services.UserService;
import com.cloudova.service.utils.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthenticationTokenService {

    public final static int tokenExpirationDays = 10;

    private final AuthenticationTokenRepository repository;
    private final UserService userService;


    @Autowired
    public AuthenticationTokenService(AuthenticationTokenRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public AuthenticationToken findByRefreshToken(String refreshToken) {
        return this.repository.findByRefreshToken(refreshToken).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Optional<AuthenticationToken> findById(Long id) {
        return this.repository.findById(id);
    }

    public AuthenticationToken createAuthenticationToken(User user) {
        return this.repository.save(
                AuthenticationToken.builder()
                        .createdBy(user)
                        .refreshToken(this.generateRefreshToeken())
                        .expireDate(LocalDate.now().plusDays(AuthenticationTokenService.tokenExpirationDays))
                        .build()
        );
    }

    public AuthenticationToken createAuthenticationToken(String username) {
        return this.createAuthenticationToken(this.userService.findByUsername(username));
    }

    public void deleteAuthenticationToken(Long id) {
        this.repository.deleteById(id);
    }

    public void deleteAuthenticationToken(AuthenticationToken accessToken) {
        this.repository.deleteById(accessToken.getId());
    }

    public String generateRefreshToeken() {
        return SecureUtils.generateRandomString(32, s -> !this.repository.existsByRefreshToken(s));
    }

}
