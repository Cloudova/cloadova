package com.cloudova.service.user.repositories;

import com.cloudova.service.user.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OTPRepository extends JpaRepository<OTP, UUID> {
    @Query(
            "select o from OTP o where o.token = :token and o.identifier = :identifier and o.expiresOn < current_time()"
    )
    Optional<OTP> findByTokenAndIdentifier(String token, String identifier);
    boolean existsByToken(String token);
}