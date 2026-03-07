package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeStatusRequest(
        @NotBlank(message = "Status is required")
        String status) {
}
