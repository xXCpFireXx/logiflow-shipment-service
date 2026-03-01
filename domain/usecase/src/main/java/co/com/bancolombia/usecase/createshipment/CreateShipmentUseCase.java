package co.com.bancolombia.usecase.createshipment;

import co.com.bancolombia.model.coordinates.Coordinates;
import co.com.bancolombia.model.coordinates.gateways.GeocodingGateway;
import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import co.com.bancolombia.model.trackingEvent.TrackingEvent;
import co.com.bancolombia.model.trackingEvent.gateways.TrackingEventGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateShipmentUseCase {

    private final ShipmentRepository shipmentRepository;
    private final TrackingEventGateway trackingEventGateway;
    private final GeocodingGateway geocodingGateway;

    public Mono<Shipment> createShipment(Shipment rawShipment) {

        String newId = UUID.randomUUID().toString();
        Shipment businessShipment = new Shipment(
                newId,
                rawShipment.getOrigin(),
                rawShipment.getDestination(),
                rawShipment.getCustomer(),
                rawShipment.getCargo()
        );

// 1. Primero guardamos en MongoDB
        return shipmentRepository.save(businessShipment)
                // 2. Ejecutamos cuando se guarda exitosamente
                .flatMap(savedShipment -> {

                    String originCity = (savedShipment.getOrigin() != null) ? savedShipment.getOrigin().getCity() : "Ciudad Desconocida";
                    String originCountry = (savedShipment.getOrigin() != null) ? savedShipment.getOrigin().getCountry() : "XX";

                    // 3. Buscamos las coordenadas llamando a nuestro Gateway
                    return geocodingGateway.getCoordinates(originCity, originCountry)
                            // Si el servicio de mapas falla, damos unas coordenadas por defecto para no romper el proceso
                            .onErrorReturn(new Coordinates(0.0, 0.0))
                            .flatMap(coords -> {

                                // 4. Construimos el evento con las coordenadas reales
                                TrackingEvent event = TrackingEvent.builder()
                                        .shipmentId(savedShipment.getId())
                                        .status("CREATED")
                                        .description("Envío registrado en origen: " + originCity)
                                        .city(originCity)
                                        .countryCode(originCountry)
                                        .latitude(coords.getLatitude())   // <-- Usamos la Latitud real
                                        .longitude(coords.getLongitude()) // <-- Usamos la Longitud real
                                        .build();

                                // 5. Enviamos el evento a Tracking
                                return trackingEventGateway.sendTrackingEvent(event);
                            })
                            .onErrorResume(error -> {
                                System.err.println("Fallo al enviar evento a Tracking: " + error.getMessage());
                                return Mono.empty();
                            })
                            .thenReturn(savedShipment);
                });
    }
}