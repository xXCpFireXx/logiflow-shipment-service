package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.ShipmentRequest;
import co.com.bancolombia.api.mapper.ShipmentRestMapper;
import co.com.bancolombia.usecase.changeshipmentstatus.ChangeShipmentStatusUseCase;
import co.com.bancolombia.usecase.createshipment.CreateShipmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ShipmentHandler {
    private final CreateShipmentUseCase CreateUseCase;
    private final ShipmentRestMapper mapper;

    public Mono<ServerResponse> createShipment(ServerRequest request) {
        return request.bodyToMono(ShipmentRequest.class) // 1. Convertir JSON a DTO Request
                .map(mapper::toDomain)                   // 2. DTO -> Dominio
                .flatMap(CreateUseCase::createShipment)  // 3. Ejecutar caso de uso (Devuelve Mono<Shipment>)
                .flatMap(shipment -> ServerResponse
                        .created(URI.create("/shipments/" + shipment.getId()))
                        .build());
    }

//    public Mono<ServerResponse> updateStatusShipment(ServerRequest request) {
//        return request.bodyToMono(ShipmentRequest.class)
//                .map(mapper::toDomain)
//                .flatMap(ChangeShipmentStatusUseCase::changeShipmentStatus)
//                .flatMap(shipment-> ServerResponse.ok().build());
//    }

//    // --- 2. CONSULTAR POR ID (GET) ---
//    public Mono<ServerResponse> getShipmentById(ServerRequest request) {
//        String id = request.pathVariable("id");
//
//        return useCase.findById(id)                      // 1. Busca en BD (Trae los datos mockeados guardados)
//                .map(mapper::toResponse)                 // 2. Construye el JSON 'details' visual para el front
//                .flatMap(response -> ServerResponse.ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(response))
//                // Manejo básico de error 404 si es vacío
//                .switchIfEmpty(ServerResponse.notFound().build());
//    }
//
//    // --- 3. CONSULTAR TODOS PAGINADO (GET) ---
//    public Mono<ServerResponse> getAllShipments(ServerRequest request) {
//        // Leemos query params: ?page=0&size=10
//        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
//        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
//
//        return useCase.getAllShipments(page, size)
//                // OJO: Aquí tu UseCase devuelve PageResult<Shipment>
//                // Podrías mapear el contenido de la página a Response si quisieras
//                // .map(pageResult -> ... convertir lista interna ...)
//                .flatMap(pageResult -> ServerResponse.ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(pageResult));
//    }
}