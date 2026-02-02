package co.com.bancolombia.usecase.createshipment;

import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateShipmentUseCase {

    private final ShipmentRepository shipmentRepository;

    public Mono<Shipment> createShipment(Shipment rawShipment) {

        String newId = UUID.randomUUID().toString();
        Shipment businessShipment = new Shipment(
                newId,
                rawShipment.getOrigin(),
                rawShipment.getDestination(),
                rawShipment.getCustomer(),
                rawShipment.getCargo()
        );

        return shipmentRepository.save(businessShipment);
    }
}
