package com.interview.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "hikes")
@FieldNameConstants
public class Hike extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, updatable = false)
    private UUID externalId = UUID.randomUUID();

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "weather")
    private String weather;

    @Column(name = "notes", length = 1000)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trail_id", nullable = false)
    private Trail trail;

    @Column(name = "is_public")
    private boolean isPublic = false;
}
