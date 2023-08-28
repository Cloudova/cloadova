package com.soroosh.auth.jwt.services;

import com.soroosh.auth.commons.models.BaseUser;
import com.soroosh.auth.jwt.models.AuthenticationToken;
import com.soroosh.auth.jwt.models.AuthenticationTokenRepository;
import com.soroosh.auth.utils.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthenticationTokenService {

    public final static int tokenExpirationDays = 10;

    private final AuthenticationTokenRepository repository;

    @Autowired
    public AuthenticationTokenService(AuthenticationTokenRepository repository) {
        this.repository = repository;
    }

    public Optional<AuthenticationToken> findById(Long id) {
        return this.repository.findById(id);
    }

    public AuthenticationToken createAuthenticationToken(BaseUser user) {
        return this.repository.save(
                AuthenticationToken.builder()
                        .createdBy(user)
                        .refreshToken(this.generateRefreshToken())
                        .expireDate(LocalDate.now().plusDays(AuthenticationTokenService.tokenExpirationDays))
                        .build()
        );
    }

    public void deleteAuthenticationToken(AuthenticationToken accessToken) {
        this.repository.deleteById(accessToken.getId());
    }

    public String generateRefreshToken() {
        return SecureUtils.generateRandomString(32, s -> !this.repository.existsByRefreshToken(s));
    }

}
