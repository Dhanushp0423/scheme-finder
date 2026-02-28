package com.example.scheme.finder.service;

import com.example.scheme.finder.dto.CategoryDto;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import com.example.scheme.finder.entity.SavedScheme;
import com.example.scheme.finder.entity.Scheme;
import com.example.scheme.finder.entity.User;
import com.example.scheme.finder.exception.BadRequestException;
import com.example.scheme.finder.exception.ResourceNotFoundException;
import com.example.scheme.finder.repository.SavedSchemeRepository;
import com.example.scheme.finder.repository.SchemeRepository;
import com.example.scheme.finder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavedSchemeServiceImpl implements SavedSchemeService {

    private final SavedSchemeRepository savedSchemeRepository;
    private final SchemeRepository schemeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void saveScheme(Long userId, Long schemeId) {
        if (savedSchemeRepository.existsByUserIdAndSchemeId(userId, schemeId)) {
            throw new BadRequestException("Scheme is already saved");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Scheme scheme = schemeRepository.findById(schemeId)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", schemeId));

        SavedScheme savedScheme = SavedScheme.builder()
                .user(user)
                .scheme(scheme)
                .build();

        savedSchemeRepository.save(savedScheme);
    }

    @Override
    @Transactional
    public void unsaveScheme(Long userId, Long schemeId) {
        if (!savedSchemeRepository.existsByUserIdAndSchemeId(userId, schemeId)) {
            throw new BadRequestException("Scheme is not saved");
        }
        savedSchemeRepository.deleteByUserIdAndSchemeId(userId, schemeId);
    }

    @Override
    public PagedResponse<SchemeDto.SchemeSummary> getSavedSchemes(Long userId, Pageable pageable) {
        Page<SavedScheme> saved = savedSchemeRepository.findByUserId(userId, pageable);
        return PagedResponse.of(saved.map(ss -> mapToSummary(ss.getScheme(), userId)));
    }

    @Override
    @Transactional
    public boolean isSchemesSaved(Long userId, Long schemeId) {
        return savedSchemeRepository.existsByUserIdAndSchemeId(userId, schemeId);
    }

    private SchemeDto.SchemeSummary mapToSummary(Scheme scheme, Long userId) {
        CategoryDto.CategoryResponse catDto = null;
        if (scheme.getCategory() != null) {
            catDto = CategoryDto.CategoryResponse.builder()
                    .id(scheme.getCategory().getId())
                    .name(scheme.getCategory().getName())
                    .iconUrl(scheme.getCategory().getIconUrl())
                    .colorCode(scheme.getCategory().getColorCode())
                    .build();
        }
        return SchemeDto.SchemeSummary.builder()
                .id(scheme.getId())
                .title(scheme.getTitle())
                .shortDescription(scheme.getShortDescription())
                .category(catDto)
                .ministryName(scheme.getMinistryName())
                .schemeLevel(scheme.getSchemeLevel())
                .stateName(scheme.getStateName())
                .status(scheme.getStatus())
                .benefitType(scheme.getBenefitType())
                .benefitAmount(scheme.getBenefitAmount())
                .viewCount(scheme.getViewCount())
                .isFeatured(scheme.getIsFeatured())
                .isSaved(true)
                .createdAt(scheme.getCreatedAt())
                .build();
    }
}
