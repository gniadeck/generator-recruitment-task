package dev.komp15.generatorrecruitmenttask.dto;

import java.time.LocalDateTime;

public record JobDTO(
        Long id,
        LocalDateTime postedDate
) {
}
