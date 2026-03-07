package co.com.bancolombia.usecase.changeshipmentstatus;

import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.ShipmentStatus;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ChangeShipmentStatusUseCase {
    private final ShipmentRepository shipmentRepository;

    public Mono<Shipment> changeShipmentStatus(String shipmentId, ShipmentStatus newStatus) {
        return shipmentRepository.findById(shipmentId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Shipment not found!")))
                .map(shipment -> {
                    switch (newStatus) {
                        case IN_TRANSIT -> shipment.startTransit();
                        case AT_WAREHOUSE -> shipment.arriveAtWarehouse();
                        case OUT_FOR_DELIVERY -> shipment.outForDelivery();
                        case DELIVERED -> shipment.deliver();
                        case INCIDENT -> shipment.markIncident();
                        default -> {throw new IllegalArgumentException("Invalid ShipmentStatus!");}
                    }
                    return shipment;
                })
                .flatMap(shipmentRepository::save);
    }
}
