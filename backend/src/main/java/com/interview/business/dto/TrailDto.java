package com.interview.business.dto;

import com.interview.data.enumeration.TrailDifficulty;

public record TrailDto(Long id,
                       String location,
                       Double distanceKm,
                       Integer elevationGainMeters,
                       TrailDifficulty difficulty,
                       String description,
                       String latitude,
                       String longitude,
                       Integer placeRank) {
}
