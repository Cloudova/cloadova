package com.soroosh.auth.jwt.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Long> {
    Optional<AuthenticationToken> findByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);

}