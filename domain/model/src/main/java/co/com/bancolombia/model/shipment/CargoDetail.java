package co.com.bancolombia.model.shipment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class CargoDetail {
    private final String packageType;
    private final String quantity;
    private final String dimensions;
    private final String volume;
    private final String commodity;
    private final boolean stackable;
    private final String hsCode;
    private final Double weight;
}
