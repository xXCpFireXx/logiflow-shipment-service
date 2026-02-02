package co.com.bancolombia.api.dto;
import jakarta.validation.constraints.NotBlank;

public record ShipmentRequest(
        @NotBlank String origin,
        @NotBlank String destination,
        @NotBlank String customerId
) {
}
