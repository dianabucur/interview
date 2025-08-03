package com.interview.business.dto;

import com.interview.data.enumeration.TrailDifficulty;

import java.time.LocalDate;

public record HikeListingDto(Long id,
                             LocalDate date,
                             Double durationHours,
                             String userDisplayName,
                             String trailName,
                             String trailLocation,
                             Double distanceKm,
                             Integer elevationGainMeters,
                             TrailDifficulty difficulty) {
}
