package com.example.scheme.finder.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schemes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scheme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "full_description", columnDefinition = "LONGTEXT")
    private String fullDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "ministry_name")
    private String ministryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "scheme_level")
    private SchemeLevel schemeLevel; // CENTRAL, STATE

    @Column(name = "state_name")
    private String stateName; // For state-level schemes

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "official_website")
    private String officialWebsite;

    @Column(name = "application_url")
    private String applicationUrl;

    @Column(name = "helpline_number")
    private String helplineNumber;

    @Enumerated(EnumType.STRING)
    private SchemeStatus status;

    // Eligibility fields
    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_eligibility")
    private GenderEligibility genderEligibility; // ALL, MALE, FEMALE

    @Column(name = "eligible_categories", columnDefinition = "TEXT")
    private String eligibleCategories; // Comma-separated: General,OBC,SC,ST

    @Column(name = "max_annual_income")
    private Double maxAnnualIncome;

    @Column(name = "is_for_disabled")
    private Boolean isForDisabled;

    @Column(name = "is_for_students")
    private Boolean isForStudents;

    @Column(name = "is_for_employed")
    private Boolean isForEmployed;

    @Column(name = "eligible_states", columnDefinition = "TEXT")
    private String eligibleStates; // "ALL" or comma-separated states

    // Benefits
    @Column(name = "benefit_type")
    private String benefitType; // Financial, Non-Financial, Both

    @Column(name = "benefit_amount")
    private String benefitAmount;

    @Column(name = "eligibility_criteria", columnDefinition = "TEXT")
    private String eligibilityCriteria;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    // Documents required
    @Column(name = "documents_required", columnDefinition = "TEXT")
    private String documentsRequired;

    // Application process
    @Column(name = "application_process", columnDefinition = "TEXT")
    private String applicationProcess;

    // Tags
    @Column(columnDefinition = "TEXT")
    private String tags;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchemeFaq> faqs;

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SavedScheme> savedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchemeApplication> applications;

    public enum SchemeLevel {
        CENTRAL, STATE
    }

    public enum SchemeStatus {
        ACTIVE, INACTIVE, UPCOMING, EXPIRED
    }

    public enum GenderEligibility {
        ALL, MALE, FEMALE
    }
}