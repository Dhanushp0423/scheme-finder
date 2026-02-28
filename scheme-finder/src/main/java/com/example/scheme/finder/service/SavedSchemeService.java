package com.example.scheme.finder.service;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import org.springframework.data.domain.Pageable;

public interface SavedSchemeService {
    void saveScheme(Long userId, Long schemeId);
    void unsaveScheme(Long userId, Long schemeId);
    PagedResponse<SchemeDto.SchemeSummary> getSavedSchemes(Long userId, Pageable pageable);
    boolean isSchemesSaved(Long userId, Long schemeId);
}