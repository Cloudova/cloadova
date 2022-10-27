package com.cloudova.service.project.repositories;

import com.cloudova.service.project.models.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {
    boolean existsBySubdomain(String subdomain);
    long countByUser_Id(Long id);

    Page<Application> findByUserId(Long id, Pageable pageable);



}