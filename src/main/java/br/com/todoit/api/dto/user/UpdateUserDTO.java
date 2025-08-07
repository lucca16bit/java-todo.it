package br.com.todoit.api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO(
        @NotBlank
        String name,
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
