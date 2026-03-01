package co.com.bancolombia.model.trackingEvent;
import lombok.*;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class TrackingEvent {
    private String shipmentId;
    private String status;
    private String description;
    private String city;
    private String countryCode;
    private double latitude;
    private double longitude;
}