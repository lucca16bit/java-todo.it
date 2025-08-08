package br.com.todoit.api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(

        @NotBlank
        String name,

        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
