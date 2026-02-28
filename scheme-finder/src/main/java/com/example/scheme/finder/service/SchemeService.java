package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SchemeService {
    SchemeDto.SchemeResponse createScheme(SchemeDto.CreateSchemeRequest request);
    SchemeDto.SchemeResponse updateScheme(Long id, SchemeDto.CreateSchemeRequest request);
    SchemeDto.SchemeResponse getSchemeById(Long id, Long userId);
    PagedResponse<SchemeDto.SchemeSummary> getAllSchemes(Pageable pageable, Long userId);
    PagedResponse<SchemeDto.SchemeSummary> getSchemesByCategory(Long categoryId, Pageable pageable, Long userId);
    PagedResponse<SchemeDto.SchemeSummary> getSchemesByState(String stateName, Pageable pageable, Long userId);
    PagedResponse<SchemeDto.SchemeSummary> searchSchemes(String keyword, Pageable pageable, Long userId);
    PagedResponse<SchemeDto.SchemeSummary> getEligibleSchemes(SchemeDto.EligibilityFilterRequest filter, Pageable pageable, Long userId);
    List<SchemeDto.SchemeSummary> getFeaturedSchemes(Long userId);
    void deleteScheme(Long id);
    SchemeDto.SchemeStats getStats();
}
