package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ShipmentSummaryResponse(
        @JsonProperty("_id")String id,
        String trackingId,
        LocalDate eta,
        String origin,
        String destination,
        String customer,
        String status
) {
}
