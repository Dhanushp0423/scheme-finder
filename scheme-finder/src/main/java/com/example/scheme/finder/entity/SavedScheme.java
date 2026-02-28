package com.example.scheme.finder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved_schemes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "scheme_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedScheme extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;
}