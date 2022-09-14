package com.cloudova.service.project.repositories;

import com.cloudova.service.project.models.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {
}