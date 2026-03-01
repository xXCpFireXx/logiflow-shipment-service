package co.com.bancolombia.model.trackingEvent.gateways;

import co.com.bancolombia.model.trackingEvent.TrackingEvent;
import reactor.core.publisher.Mono;

public interface TrackingEventGateway {
    // Contrato: Recibe el modelo que acabamos de crear y retorna un Mono<Void>
    Mono<Void> sendTrackingEvent(TrackingEvent trackingEvent);
}
