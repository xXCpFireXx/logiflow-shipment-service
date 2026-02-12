package co.com.bancolombia.api.dto;
import co.com.bancolombia.model.shipment.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ShipmentRequest(
        @NotBlank(message = "Origin is required")
        @Valid
        LocationDto origin,

        @NotBlank(message = "Destination is required")
        @Valid
        LocationDto destination,

        @NotBlank(message = "ID Client is required")
        String customerId
) {
    public record LocationDto(
            @NotBlank(message = "City is required")
            String city,

            @NotBlank(message = "Country is required")
            String country
    ) {}
}
