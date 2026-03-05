package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record ShipmentResponse(
        @JsonProperty("_id") String id,
        String trackingNumber,
        LocalDate eta,
        String origin,
        String destination,
        String customer,
        String status,

        UiDetailsResponse details,
        CargoDetailResponse cargo,
        List<DocumentResponse> documents
) {
    public record UiDetailsResponse(UiItem origin, UiItem destination, UiItem carrier, UiItem weight) {}
    public record UiItem(String label, String value, String subtext) {}
    public record CargoDetailResponse(
            String packageType,
            String quantity,
            String dimensions,
            String volume,
            String commodity,
            Boolean stackable,
            String hsCode,
            Double weight
    ) {}
    public record DocumentResponse(String name, String format, String size) {}
}