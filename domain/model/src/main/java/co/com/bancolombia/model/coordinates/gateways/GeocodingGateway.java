package co.com.bancolombia.model.coordinates.gateways;

import co.com.bancolombia.model.coordinates.Coordinates;
import reactor.core.publisher.Mono;

public interface GeocodingGateway {
    Mono<Coordinates> getCoordinates(String city, String country);
}
