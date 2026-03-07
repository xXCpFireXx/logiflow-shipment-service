package co.com.bancolombia.model.shipment.gateways;

import co.com.bancolombia.model.common.PageResult;
import co.com.bancolombia.model.shipment.Shipment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ShipmentRepository {
    Mono<Shipment> save(Shipment shipment);
    Mono<Shipment> findById(String id);
    Mono<PageResult<Shipment>> findAll(int page, int size);
    Flux<Shipment> findAll();
}
