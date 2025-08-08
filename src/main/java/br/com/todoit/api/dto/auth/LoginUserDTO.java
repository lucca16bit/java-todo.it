package br.com.todoit.api.dto.auth;

public record LoginUserDTO(
        String login,
        String password
) {
}
