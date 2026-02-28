package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.CategoryDto;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import com.example.scheme.finder.entity.Category;
import com.example.scheme.finder.entity.Scheme;
import com.example.scheme.finder.entity.SchemeFaq;
import com.example.scheme.finder.exception.ResourceNotFoundException;
import com.example.scheme.finder.repository.CategoryRepository;
import com.example.scheme.finder.repository.SavedSchemeRepository;
import com.example.scheme.finder.repository.SchemeRepository;
import com.example.scheme.finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SchemeServiceImpl implements SchemeService {

    private final SchemeRepository schemeRepository;
    private final CategoryRepository categoryRepository;
    private final SavedSchemeRepository savedSchemeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SchemeDto.SchemeResponse createScheme(SchemeDto.CreateSchemeRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        Scheme scheme = Scheme.builder()
                .title(request.getTitle())
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .category(category)
                .ministryName(request.getMinistryName())
                .schemeLevel(request.getSchemeLevel())
                .stateName(request.getStateName())
                .launchDate(request.getLaunchDate())
                .endDate(request.getEndDate())
                .officialWebsite(request.getOfficialWebsite())
                .applicationUrl(request.getApplicationUrl())
                .helplineNumber(request.getHelplineNumber())
                .status(request.getStatus() != null ? request.getStatus() : Scheme.SchemeStatus.ACTIVE)
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .genderEligibility(request.getGenderEligibility() != null ? request.getGenderEligibility() : Scheme.GenderEligibility.ALL)
                .eligibleCategories(request.getEligibleCategories())
                .maxAnnualIncome(request.getMaxAnnualIncome())
                .isForDisabled(request.getIsForDisabled())
                .isForStudents(request.getIsForStudents())
                .isForEmployed(request.getIsForEmployed())
                .eligibleStates(request.getEligibleStates())
                .benefitType(request.getBenefitType())
                .benefitAmount(request.getBenefitAmount())
                .benefits(request.getBenefits())
                .documentsRequired(request.getDocumentsRequired())
                .applicationProcess(request.getApplicationProcess())
                .tags(request.getTags())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .viewCount(0L)
                .build();

        Scheme saved = schemeRepository.save(scheme);

        // Save FAQs
        if (request.getFaqs() != null && !request.getFaqs().isEmpty()) {
            List<SchemeFaq> faqs = request.getFaqs().stream().map(faqReq -> SchemeFaq.builder()
                    .scheme(saved)
                    .question(faqReq.getQuestion())
                    .answer(faqReq.getAnswer())
                    .displayOrder(faqReq.getDisplayOrder() != null ? faqReq.getDisplayOrder() : 0)
                    .build()).collect(Collectors.toList());
            saved.setFaqs(faqs);
            schemeRepository.save(saved);
        }

        return mapToSchemeResponse(saved, null);
    }

    @Override
    @Transactional
    public SchemeDto.SchemeResponse updateScheme(Long id, SchemeDto.CreateSchemeRequest request) {
        Scheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        scheme.setTitle(request.getTitle());
        scheme.setShortDescription(request.getShortDescription());
        scheme.setFullDescription(request.getFullDescription());
        scheme.setCategory(category);
        scheme.setMinistryName(request.getMinistryName());
        scheme.setSchemeLevel(request.getSchemeLevel());
        scheme.setStateName(request.getStateName());
        scheme.setLaunchDate(request.getLaunchDate());
        scheme.setEndDate(request.getEndDate());
        scheme.setOfficialWebsite(request.getOfficialWebsite());
        scheme.setApplicationUrl(request.getApplicationUrl());
        scheme.setHelplineNumber(request.getHelplineNumber());
        if (request.getStatus() != null) scheme.setStatus(request.getStatus());
        scheme.setMinAge(request.getMinAge());
        scheme.setMaxAge(request.getMaxAge());
        if (request.getGenderEligibility() != null) scheme.setGenderEligibility(request.getGenderEligibility());
        scheme.setEligibleCategories(request.getEligibleCategories());
        scheme.setMaxAnnualIncome(request.getMaxAnnualIncome());
        scheme.setIsForDisabled(request.getIsForDisabled());
        scheme.setIsForStudents(request.getIsForStudents());
        scheme.setIsForEmployed(request.getIsForEmployed());
        scheme.setEligibleStates(request.getEligibleStates());
        scheme.setBenefitType(request.getBenefitType());
        scheme.setBenefitAmount(request.getBenefitAmount());
        scheme.setBenefits(request.getBenefits());
        scheme.setDocumentsRequired(request.getDocumentsRequired());
        scheme.setApplicationProcess(request.getApplicationProcess());
        scheme.setTags(request.getTags());
        if (request.getIsFeatured() != null) scheme.setIsFeatured(request.getIsFeatured());

        Scheme updated = schemeRepository.save(scheme);
        return mapToSchemeResponse(updated, null);
    }

    @Override
    @Transactional
    public SchemeDto.SchemeResponse getSchemeById(Long id, Long userId) {
        Scheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        schemeRepository.incrementViewCount(id);
        return mapToSchemeResponse(scheme, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<SchemeDto.SchemeSummary> getAllSchemes(Pageable pageable, Long userId) {
        Page<Scheme> schemes = schemeRepository.findByStatus(Scheme.SchemeStatus.ACTIVE, pageable);
        return PagedResponse.of(schemes.map(s -> mapToSchemeSummary(s, userId)));
    }

    @Override
    @Transactional
    public PagedResponse<SchemeDto.SchemeSummary> getSchemesByCategory(Long categoryId, Pageable pageable, Long userId) {
        Page<Scheme> schemes = schemeRepository.findByCategoryIdAndStatus(categoryId, Scheme.SchemeStatus.ACTIVE, pageable);
        return PagedResponse.of(schemes.map(s -> mapToSchemeSummary(s, userId)));
    }

    @Override
    @Transactional
    public PagedResponse<SchemeDto.SchemeSummary> getSchemesByState(String stateName, Pageable pageable, Long userId) {
        Page<Scheme> schemes = schemeRepository.findByStateNameAndStatus(stateName, Scheme.SchemeStatus.ACTIVE, pageable);
        return PagedResponse.of(schemes.map(s -> mapToSchemeSummary(s, userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<SchemeDto.SchemeSummary> searchSchemes(String keyword, Pageable pageable, Long userId) {
        Page<Scheme> schemes = schemeRepository.searchByKeyword(keyword, pageable);
        return PagedResponse.of(schemes.map(s -> mapToSchemeSummary(s, userId)));
    }

    @Override
    @Transactional
    public PagedResponse<SchemeDto.SchemeSummary> getEligibleSchemes(SchemeDto.EligibilityFilterRequest filter, Pageable pageable, Long userId) {
        Scheme.GenderEligibility gender = null;
        if (filter.getGender() != null && !filter.getGender().isEmpty()) {
            try {
                gender = Scheme.GenderEligibility.valueOf(filter.getGender().toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        Page<Scheme> schemes = schemeRepository.findEligibleSchemes(
                filter.getAge(),
                filter.getAge(),
                filter.getAge(),
                gender,
                filter.getAnnualIncome(),
                filter.getIsDisabled() != null ? filter.getIsDisabled() : false,
                filter.getIsStudent() != null ? filter.getIsStudent() : false,
                filter.getIsEmployed() != null ? filter.getIsEmployed() : false,
                pageable
        );

        return PagedResponse.of(schemes.map(s -> mapToSchemeSummary(s, userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchemeDto.SchemeSummary> getFeaturedSchemes(Long userId) {
        List<Scheme> schemes = schemeRepository.findByIsFeaturedTrueAndStatus(Scheme.SchemeStatus.ACTIVE);
        return schemes.stream().map(s -> mapToSchemeSummary(s, userId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteScheme(Long id) {
        Scheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        schemeRepository.delete(scheme);
    }

    @Override
    @Transactional
    public SchemeDto.SchemeStats getStats() {
        long totalSchemes = schemeRepository.count();
        long activeSchemes = schemeRepository.countActiveSchemes();
        long totalUsers = userRepository.count();

        return SchemeDto.SchemeStats.builder()
                .totalSchemes(totalSchemes)
                .activeSchemes(activeSchemes)
                .totalUsers(totalUsers)
                .build();
    }

    // --- Mapper Methods ---

    private SchemeDto.SchemeResponse mapToSchemeResponse(Scheme scheme, Long userId) {
        Boolean isSaved = userId != null && savedSchemeRepository.existsByUserIdAndSchemeId(userId, scheme.getId());

        List<SchemeDto.FaqResponse> faqs = scheme.getFaqs() != null
                ? scheme.getFaqs().stream()
                .map(faq -> SchemeDto.FaqResponse.builder()
                        .id(faq.getId())
                        .question(faq.getQuestion())
                        .answer(faq.getAnswer())
                        .displayOrder(faq.getDisplayOrder())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        return SchemeDto.SchemeResponse.builder()
                .id(scheme.getId())
                .title(scheme.getTitle())
                .shortDescription(scheme.getShortDescription())
                .fullDescription(scheme.getFullDescription())
                .category(mapToCategoryResponse(scheme.getCategory()))
                .ministryName(scheme.getMinistryName())
                .schemeLevel(scheme.getSchemeLevel())
                .stateName(scheme.getStateName())
                .launchDate(scheme.getLaunchDate())
                .endDate(scheme.getEndDate())
                .officialWebsite(scheme.getOfficialWebsite())
                .applicationUrl(scheme.getApplicationUrl())
                .helplineNumber(scheme.getHelplineNumber())
                .status(scheme.getStatus())
                .minAge(scheme.getMinAge())
                .maxAge(scheme.getMaxAge())
                .genderEligibility(scheme.getGenderEligibility())
                .eligibleCategories(scheme.getEligibleCategories())
                .maxAnnualIncome(scheme.getMaxAnnualIncome())
                .isForDisabled(scheme.getIsForDisabled())
                .isForStudents(scheme.getIsForStudents())
                .isForEmployed(scheme.getIsForEmployed())
                .eligibleStates(scheme.getEligibleStates())
                .benefitType(scheme.getBenefitType())
                .benefitAmount(scheme.getBenefitAmount())
                .benefits(scheme.getBenefits())
                .documentsRequired(scheme.getDocumentsRequired())
                .applicationProcess(scheme.getApplicationProcess())
                .tags(scheme.getTags())
                .viewCount(scheme.getViewCount())
                .isFeatured(scheme.getIsFeatured())
                .faqs(faqs)
                .createdAt(scheme.getCreatedAt())
                .isSaved(isSaved)
                .build();
    }

    private SchemeDto.SchemeSummary mapToSchemeSummary(Scheme scheme, Long userId) {
        Boolean isSaved = userId != null && savedSchemeRepository.existsByUserIdAndSchemeId(userId, scheme.getId());

        return SchemeDto.SchemeSummary.builder()
                .id(scheme.getId())
                .title(scheme.getTitle())
                .shortDescription(scheme.getShortDescription())
                .category(mapToCategoryResponse(scheme.getCategory()))
                .ministryName(scheme.getMinistryName())
                .schemeLevel(scheme.getSchemeLevel())
                .stateName(scheme.getStateName())
                .status(scheme.getStatus())
                .benefitType(scheme.getBenefitType())
                .benefitAmount(scheme.getBenefitAmount())
                .viewCount(scheme.getViewCount())
                .isFeatured(scheme.getIsFeatured())
                .isSaved(isSaved)
                .createdAt(scheme.getCreatedAt())
                .build();
    }

    private CategoryDto.CategoryResponse mapToCategoryResponse(Category category) {
        if (category == null) return null;
        return CategoryDto.CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .colorCode(category.getColorCode())
                .isActive(category.getIsActive())
                .displayOrder(category.getDisplayOrder())
                .build();
    }
}