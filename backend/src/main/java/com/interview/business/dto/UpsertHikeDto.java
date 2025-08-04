package com.interview.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpsertHikeDto(@NotNull(message = "{validation.hike.date.required}")
                            LocalDate date,
                            Double durationHours,
                            String weather,
                            @Size(max = 1000, message = "{validation.hike.notes.max}")
                            @Schema(maxLength = 1000)
                            String notes,
                            @Schema(description = "Flag that marks if a hiking log will be visible to others or not", defaultValue = "false")
                            Boolean isPublic,
                            @NotNull(message = "{validation.hike.trail.required}")
                            Long trailId) {
}
