package com.interview.business.dto;

import java.time.LocalDate;

public record HikeDto(Long id,
                      LocalDate date,
                      Double durationHours,
                      String weather,
                      String notes,
                      TrailDto trail) {
}
