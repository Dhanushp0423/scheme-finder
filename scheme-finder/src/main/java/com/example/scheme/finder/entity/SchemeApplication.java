package com.example.scheme.finder.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scheme_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "reference_number")
    private String referenceNumber;

    public enum ApplicationStatus {
        INITIATED, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, WITHDRAWN
    }
}