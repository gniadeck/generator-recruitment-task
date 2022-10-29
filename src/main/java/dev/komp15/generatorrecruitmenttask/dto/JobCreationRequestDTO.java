package dev.komp15.generatorrecruitmenttask.dto;

import lombok.Builder;

@Builder
public record JobCreationRequestDTO(
        Long minLength,
        Long maxLength,
        Character[] chars,
        Long jobSize) {
}
