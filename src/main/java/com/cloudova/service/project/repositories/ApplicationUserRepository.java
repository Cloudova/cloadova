package com.cloudova.service.project.repositories;

import com.cloudova.service.project.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    @Query(
            "SELECT a FROM ApplicationUser a where (a.username = :username OR a.email = :username) and a.application.subdomain = :subdomain"
    )
    Optional<ApplicationUser> findByUsernameOrEmail(@Param("username") String username, @Param("subdomain") String subdomain);

}