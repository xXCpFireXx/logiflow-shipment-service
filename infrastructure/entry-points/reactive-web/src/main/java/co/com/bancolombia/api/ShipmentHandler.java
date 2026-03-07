package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.ChangeStatusRequest;
import co.com.bancolombia.api.dto.ShipmentRequest;
import co.com.bancolombia.api.dto.ShipmentSummaryResponse;
import co.com.bancolombia.api.mapper.ShipmentRestMapper;
import co.com.bancolombia.model.common.PageResult;
import co.com.bancolombia.model.shipment.ShipmentStatus;
import co.com.bancolombia.usecase.changeshipmentstatus.ChangeShipmentStatusUseCase;
import co.com.bancolombia.usecase.createshipment.CreateShipmentUseCase;
import co.com.bancolombia.usecase.getshipment.GetshipmentUseCase;
import co.com.bancolombia.usecase.syncshipments.SyncShipmentsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ShipmentHandler {
    private final CreateShipmentUseCase CreateUseCase;
    private final GetshipmentUseCase getShipmentUseCase;
    private final ChangeShipmentStatusUseCase changeShipmentStatusUseCase;
    private final SyncShipmentsUseCase syncShipmentsUseCase;
    private final ShipmentRestMapper mapper;

    public Mono<ServerResponse> createShipment(ServerRequest request) {
        return request.bodyToMono(ShipmentRequest.class) // 1. Convertir JSON a DTO Request
                .map(mapper::toDomain)                   // 2. DTO -> Dominio
                .flatMap(CreateUseCase::createShipment)  // 3. Ejecutar caso de uso (Devuelve Mono<Shipment>)
                .flatMap(shipment -> ServerResponse
                        .created(URI.create("/shipments/" + shipment.getId()))
                        .build());
    }

    public Mono<ServerResponse> getShipmentById(ServerRequest request) {
        String id = request.pathVariable("id");
        return getShipmentUseCase.getShipmentById(id)
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
//
//    // --- 3. CONSULTAR TODOS PAGINADO (GET) ---
public Mono<ServerResponse> getAll(ServerRequest request) {
    int page = Integer.parseInt(request.queryParam("page").orElse("0"));
    int size = Integer.parseInt(request.queryParam("size").orElse("10"));

    return getShipmentUseCase.getAllShipments(page, size)
            .map(pageResult -> {
                // 1. Convertimos la lista de Shipment a ShipmentSummaryResponse
                List<ShipmentSummaryResponse> summaries = pageResult.getData().stream()
                        .map(mapper::toSummaryResponse) // Usamos el nuevo método del mapper
                        .toList();

                // 2. Reconstruimos el PageResult pero ahora con el tipo ShipmentSummaryResponse
                return PageResult.<ShipmentSummaryResponse>builder()
                        .data(summaries)
                        .total(pageResult.getTotal())
                        .page(pageResult.getPage())
                        .size(pageResult.getSize())
                        .totalPages(pageResult.getTotalPages())
                        .build();
            })
            // 3. Devolvemos la respuesta al cliente
            .flatMap(mappedPage -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(mappedPage));
}

    public Mono<ServerResponse> changeStatus(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(ChangeStatusRequest.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El body de la petición es requerido")))
                .flatMap(body -> {
                    ShipmentStatus newStatus = ShipmentStatus.valueOf(body.status().toUpperCase());
                    return changeShipmentStatusUseCase.changeShipmentStatus(id, newStatus);
                })
                .flatMap(shipment -> ServerResponse.noContent().build())

                // Manejo de errores
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()))
                .onErrorResume(IllegalStateException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> syncShipments(ServerRequest request) {
        return syncShipmentsUseCase.syncAllShipments()
                .collectList() // Recolectamos todos los modificados en una lista
                .flatMap(updatedList -> ServerResponse.ok().bodyValue(
                        Map.of("message", "Sync complete", "updatedCount", updatedList.size())
                ));
    }
}