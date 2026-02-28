package com.example.scheme.finder.repository;

import com.example.scheme.finder.entity.Scheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SchemeRepository extends JpaRepository<Scheme, Long>, JpaSpecificationExecutor<Scheme> {

    Page<Scheme> findByStatus(Scheme.SchemeStatus status, Pageable pageable);

    // SchemeRepository.java
    @EntityGraph(attributePaths = {"category"})
    Page<Scheme> findByStatus(String status, Pageable pageable);

    // Or for search queries:
    @EntityGraph(attributePaths = {"category"})
    Page<Scheme> findByStatusAndTitleContaining(String status, String title, Pageable pageable);


    Page<Scheme> findByCategoryIdAndStatus(Long categoryId, Scheme.SchemeStatus status, Pageable pageable);

    Page<Scheme> findByStateNameAndStatus(String stateName, Scheme.SchemeStatus status, Pageable pageable);

    List<Scheme> findByIsFeaturedTrueAndStatus(Scheme.SchemeStatus status);

    // SchemeRepository.java
    @EntityGraph(attributePaths = {"category"})
    List<Scheme> findByIsFeaturedTrueAndStatus(String status);


        @EntityGraph(attributePaths = {"category"})
        Page<Scheme> findByCategoryIdAndStatus(Long categoryId, String status, Pageable pageable);



        @EntityGraph(attributePaths = {"category"})
        Page<Scheme> findByStatusAndTitleContainingIgnoreCase(String status, String title, Pageable pageable);





    @Query("SELECT s FROM Scheme s WHERE s.status = 'ACTIVE' AND " +
            "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.tags) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Scheme> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Scheme s SET s.viewCount = s.viewCount + 1 WHERE s.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query("SELECT s FROM Scheme s WHERE s.status = 'ACTIVE' AND " +
            "(:minAge IS NULL OR s.minAge IS NULL OR s.minAge <= :age) AND " +
            "(:maxAge IS NULL OR s.maxAge IS NULL OR s.maxAge >= :age) AND " +
            "(:gender IS NULL OR s.genderEligibility = 'ALL' OR s.genderEligibility = :gender) AND " +
            "(:income IS NULL OR s.maxAnnualIncome IS NULL OR s.maxAnnualIncome >= :income) AND " +
            "(:isDisabled = false OR s.isForDisabled = true OR s.isForDisabled IS NULL) AND " +
            "(:isStudent = false OR s.isForStudents = true OR s.isForStudents IS NULL) AND " +
            "(:isEmployed = false OR s.isForEmployed = true OR s.isForEmployed IS NULL)")
    Page<Scheme> findEligibleSchemes(
            @Param("age") Integer age,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("gender") Scheme.GenderEligibility gender,
            @Param("income") Double income,
            @Param("isDisabled") Boolean isDisabled,
            @Param("isStudent") Boolean isStudent,
            @Param("isEmployed") Boolean isEmployed,
            Pageable pageable);

    @Query("SELECT COUNT(s) FROM Scheme s WHERE s.status = 'ACTIVE'")
    Long countActiveSchemes();

    @Query("SELECT s.stateName, COUNT(s) FROM Scheme s WHERE s.schemeLevel = 'STATE' AND s.status = 'ACTIVE' GROUP BY s.stateName")
    List<Object[]> countSchemesByState();
}
