package io.github.alberes.register.manager.frontend.controllers.dto;

import java.time.LocalDateTime;

public record AddressReportDto(
        String id,
        String publicArea,
        Integer number,
        String additionalAddress,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        LocalDateTime createdDate) {
}
