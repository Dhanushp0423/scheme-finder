package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.ApplicationDto;
import com.example.scheme.finder.dto.CategoryDto;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import com.example.scheme.finder.entity.Scheme;
import com.example.scheme.finder.entity.SchemeApplication;
import com.example.scheme.finder.entity.User;
import com.example.scheme.finder.exception.BadRequestException;
import com.example.scheme.finder.exception.ResourceNotFoundException;
import com.example.scheme.finder.exception.UnauthorizedException;
import com.example.scheme.finder.repository.SchemeApplicationRepository;
import com.example.scheme.finder.repository.SchemeRepository;
import com.example.scheme.finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private final SchemeApplicationRepository applicationRepository;
    private final SchemeRepository schemeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApplicationDto.ApplicationResponse applyForScheme(Long userId, ApplicationDto.CreateApplicationRequest request) {
        if (applicationRepository.existsByUserIdAndSchemeId(userId, request.getSchemeId())) {
            throw new BadRequestException("You have already applied for this scheme");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (request.getSchemeId() == null) {
            throw new BadRequestException("schemeId is required");
        }
        Scheme scheme = schemeRepository.findById(request.getSchemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", request.getSchemeId()));

        SchemeApplication application = SchemeApplication.builder()
                .user(user)
                .scheme(scheme)
                .status(SchemeApplication.ApplicationStatus.INITIATED)
                .notes(request.getNotes())
                .referenceNumber("APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    @Transactional
    public PagedResponse<ApplicationDto.ApplicationResponse> getUserApplications(Long userId, Pageable pageable) {
        Page<SchemeApplication> applications = applicationRepository.findByUserId(userId, pageable);
        return PagedResponse.of(applications.map(this::mapToResponse));
    }

    @Override
    @Transactional
    public PagedResponse<ApplicationDto.ApplicationResponse> getSchemeApplications(Long schemeId, Pageable pageable) {
        Page<SchemeApplication> applications = applicationRepository.findBySchemeId(schemeId, pageable);
        return PagedResponse.of(applications.map(this::mapToResponse));
    }

    @Override
    @Transactional
    public ApplicationDto.ApplicationResponse updateApplicationStatus(Long applicationId, ApplicationDto.UpdateApplicationStatusRequest request) {
        SchemeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));

        application.setStatus(request.getStatus());
        if (request.getNotes() != null) application.setNotes(request.getNotes());

        return mapToResponse(applicationRepository.save(application));
    }

    @Override
    @Transactional
    public void withdrawApplication(Long userId, Long applicationId) {
        SchemeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));

        if (!application.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You cannot withdraw this application");
        }

        application.setStatus(SchemeApplication.ApplicationStatus.WITHDRAWN);
        applicationRepository.save(application);
    }

    private ApplicationDto.ApplicationResponse mapToResponse(SchemeApplication application) {
        Scheme scheme = application.getScheme();
        CategoryDto.CategoryResponse catDto = null;
        if (scheme.getCategory() != null) {
            catDto = CategoryDto.CategoryResponse.builder()
                    .id(scheme.getCategory().getId())
                    .name(scheme.getCategory().getName())
                    .build();
        }

        SchemeDto.SchemeSummary schemeSummary = SchemeDto.SchemeSummary.builder()
                .id(scheme.getId())
                .title(scheme.getTitle())
                .shortDescription(scheme.getShortDescription())
                .category(catDto)
                .ministryName(scheme.getMinistryName())
                .schemeLevel(scheme.getSchemeLevel())
                .status(scheme.getStatus())
                .build();

        return ApplicationDto.ApplicationResponse.builder()
                .id(application.getId())
                .userId(application.getUser().getId())
                .userName(application.getUser().getFullName())
                .scheme(schemeSummary)
                .status(application.getStatus())
                .notes(application.getNotes())
                .referenceNumber(application.getReferenceNumber())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
