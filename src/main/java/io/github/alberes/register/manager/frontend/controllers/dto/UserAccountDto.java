package io.github.alberes.register.manager.frontend.controllers.dto;

public record UserAccountDto(
        String id,
        String name,
        String email,
        String password,
        String role) {
}
