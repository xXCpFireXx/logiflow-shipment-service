package co.com.bancolombia.consumer;

import co.com.bancolombia.model.coordinates.Coordinates;
import co.com.bancolombia.model.coordinates.gateways.GeocodingGateway;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeocodingRestConsumer implements GeocodingGateway {

    private final WebClient webClient = WebClient.create("https://nominatim.openstreetmap.org");

    @Override
    public Mono<Coordinates> getCoordinates(String city, String country) {
        return webClient.get()
                .uri("/search?city={city}&country={country}&format=json", city, country)
                .header("User-Agent", "Logiflow-Shipment-Service/1.0")
                .retrieve()
                .bodyToFlux(NominatimResponse.class)
                .next()
                .map(response -> new Coordinates(
                        Double.parseDouble(response.getLat()),
                        Double.parseDouble(response.getLon())
                ))
                .onErrorReturn(new Coordinates(0.0, 0.0))
                .switchIfEmpty(Mono.just(new Coordinates(0.0, 0.0)));
    }

    @Data
    static class NominatimResponse {
        private String lat;
        private String lon;
    }
}
