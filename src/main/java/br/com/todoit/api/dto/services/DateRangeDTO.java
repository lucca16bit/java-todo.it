package br.com.todoit.api.dto.services;

import java.time.LocalDateTime;

public record DateRangeDTO(LocalDateTime start, LocalDateTime end) {
}
