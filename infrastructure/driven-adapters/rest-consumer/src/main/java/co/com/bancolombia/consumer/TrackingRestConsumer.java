package co.com.bancolombia.consumer;

import co.com.bancolombia.model.trackingEvent.TrackingEvent;
import co.com.bancolombia.model.trackingEvent.gateways.TrackingEventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackingRestConsumer implements TrackingEventGateway {
    private final WebClient client;

    @Override
    public Mono<Void> sendTrackingEvent(TrackingEvent trackingEvent) {
        return client.post()
                .uri("/tracking/events") // Este es el endpoint de tu otro microservicio
                .bodyValue(trackingEvent) // Spring convierte automáticamente el objeto a JSON
                .retrieve()
                .bodyToMono(Void.class);
    }
}
