package br.com.api.todolist.dto.exception;

import org.springframework.http.HttpStatus;

public record ExceptionDTO(
        HttpStatus status,
        String message) {
}
