package com.example.scheme.finder.repository;
import com.example.scheme.finder.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();
    boolean existsByName(String name);

    @Query("SELECT COUNT(s) FROM Scheme s WHERE s.category.id = :categoryId AND s.status = 'ACTIVE'")
    Long countActiveSchemesByCategoryId(@Param("categoryId") Long categoryId);
}
