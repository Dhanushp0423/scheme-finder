package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.ApplicationDto;
import com.example.scheme.finder.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {
    ApplicationDto.ApplicationResponse applyForScheme(Long userId, ApplicationDto.CreateApplicationRequest request);
    PagedResponse<ApplicationDto.ApplicationResponse> getUserApplications(Long userId, Pageable pageable);
    PagedResponse<ApplicationDto.ApplicationResponse> getSchemeApplications(Long schemeId, Pageable pageable);
    ApplicationDto.ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationDto.UpdateApplicationStatusRequest request);
    void withdrawApplication(Long userId, Long applicationId);
}
