package com.soroosh.auth.user.repositories;

import com.soroosh.auth.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileOrEmail(String mobile, String email);
}