package br.com.todoit.api.dto.exception;

import org.springframework.http.HttpStatus;

public record ExceptionDTO(
        HttpStatus status,
        String message) {
}
