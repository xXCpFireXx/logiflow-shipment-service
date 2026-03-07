package co.com.bancolombia.usecase.syncshipments;

import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.ShipmentStatus;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import co.com.bancolombia.model.trackingEvent.gateways.TrackingEventGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SyncShipmentsUseCase {
    private final ShipmentRepository shipmentRepository;
    private final TrackingEventGateway trackingGateway;

    // Retorna un Flux con los envíos que fueron efectivamente actualizados
    public Flux<Shipment> syncAllShipments() {
        return shipmentRepository.findAll() // 1. Obtenemos todos
                .flatMap(shipment ->
                        // 2. Para cada envío, preguntamos su estado actual al Tracking
                        trackingGateway.getCurrentTrackingStatus(shipment.getId())
                                .flatMap(trackingResponse -> {
                                    ShipmentStatus trackingStatus = ShipmentStatus.valueOf(trackingResponse.getStatus().toUpperCase());

                                    // 3. Comparamos. Si el estado en DB es diferente al de Tracking, lo actualizamos
                                    if (shipment.getStatus() != trackingStatus) {
                                        shipment.syncStatus(trackingStatus);
                                        return shipmentRepository.save(shipment); // Guardamos en Mongo
                                    }

                                    return Mono.empty(); // Si son iguales, no hacemos nada
                                })
                );
    }
}