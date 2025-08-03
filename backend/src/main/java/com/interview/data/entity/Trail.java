package com.interview.data.entity;

import com.interview.data.enumeration.TrailDifficulty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "trails")
@FieldNameConstants
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "elevation_gain_meters")
    private Integer elevationGainMeters;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private TrailDifficulty difficulty;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "trail", cascade = CascadeType.ALL)
    private List<Hike> hikes = new ArrayList<>();
}
