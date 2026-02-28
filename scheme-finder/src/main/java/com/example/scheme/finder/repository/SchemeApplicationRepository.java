package com.example.scheme.finder.repository;

import com.example.scheme.finder.entity.SchemeApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchemeApplicationRepository extends JpaRepository<SchemeApplication, Long> {
    Page<SchemeApplication> findByUserId(Long userId, Pageable pageable);
    Optional<SchemeApplication> findByUserIdAndSchemeId(Long userId, Long schemeId);
    boolean existsByUserIdAndSchemeId(Long userId, Long schemeId);
    Page<SchemeApplication> findBySchemeId(Long schemeId, Pageable pageable);
    long countByStatus(SchemeApplication.ApplicationStatus status);
}