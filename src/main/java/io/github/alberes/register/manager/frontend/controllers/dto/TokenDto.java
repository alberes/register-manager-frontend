package io.github.alberes.register.manager.frontend.controllers.dto;

public record TokenDto(String id, String token, Long expirationDate) {
}