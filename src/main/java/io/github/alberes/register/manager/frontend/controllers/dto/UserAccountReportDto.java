package io.github.alberes.register.manager.frontend.controllers.dto;

import java.time.LocalDateTime;

public record UserAccountReportDto(
        String id,
        String name,
        String email,
        LocalDateTime lastModifiedDate,
        LocalDateTime createdDate) {
}
