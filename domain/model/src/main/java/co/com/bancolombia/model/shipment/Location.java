package co.com.bancolombia.model.shipment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Location {
    private final String city;
    private final String country;
}
