package com.cloudova.service.project.repositories;

import com.cloudova.service.project.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsBySubdomain(String subdomain);

    long countByUser_Id(Long id);

}