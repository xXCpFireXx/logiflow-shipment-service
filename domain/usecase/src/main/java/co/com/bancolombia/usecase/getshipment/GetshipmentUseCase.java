package co.com.bancolombia.usecase.getshipment;

import co.com.bancolombia.model.common.PageResult;
import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetshipmentUseCase {
    private final ShipmentRepository shipmentRepository;

    public Mono<Shipment> getShipmentById(String id) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Shipment not found with id: " + id)));
    }

    public Mono<PageResult<Shipment>> getAllShipments(int page, int size) {
        int finalPage = Math.max(0, page);
        int finalSize = Math.max(1, size);
        return shipmentRepository.findAll(finalPage, finalSize);
    }
}
