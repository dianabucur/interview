package com.interview.business.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpsertHikeDto(@NotNull(message = "{validation.hike.date.required}")
                            LocalDate date,
                            Double durationHours,
                            String weather,
                            @Size(max = 1000, message = "{validation.hike.notes.max}")
                            String notes,
                            Boolean isPublic,
                            @NotNull(message = "{validation.hike.trail.required}")
                            Long trailId) {
}
