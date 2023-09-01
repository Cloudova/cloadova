package com.soroosh.auth.project.repositories;

import com.soroosh.auth.project.models.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, String>, PagingAndSortingRepository<Application, String> {
    boolean existsBySubdomain(String subdomain);

    long countByUser_Id(Long id);

    Page<Application> findByUserId(Long id, Pageable pageable);
}