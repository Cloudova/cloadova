package com.soroosh.auth.jwt.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationToken, Long> {
    Optional<AuthenticationToken> findByRefreshToken(String refreshToken);

    boolean existsByRefreshToken(String refreshToken);

}