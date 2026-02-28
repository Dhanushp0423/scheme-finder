package com.example.scheme.finder.repository;

import com.example.scheme.finder.entity.SavedScheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedSchemeRepository extends JpaRepository<SavedScheme, Long> {
    Optional<SavedScheme> findByUserIdAndSchemeId(Long userId, Long schemeId);
    boolean existsByUserIdAndSchemeId(Long userId, Long schemeId);
    Page<SavedScheme> findByUserId(Long userId, Pageable pageable);
    void deleteByUserIdAndSchemeId(Long userId, Long schemeId);
}
