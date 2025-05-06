package br.com.todoit.api.dto.users;

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
