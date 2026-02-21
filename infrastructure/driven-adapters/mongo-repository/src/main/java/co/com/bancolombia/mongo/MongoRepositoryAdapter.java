package co.com.bancolombia.mongo;

import co.com.bancolombia.model.common.PageResult;
import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import co.com.bancolombia.mongo.documents.ShipmentDocument;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.mapper.ShipmentPersistenceMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class MongoRepositoryAdapter
        extends AdapterOperations<Shipment, ShipmentDocument, String, MongoDBRepository>
        implements ShipmentRepository {

    private final ShipmentPersistenceMapper mapStructMapper;
    private final ReactiveMongoTemplate mongoTemplate;

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper objectMapper, ShipmentPersistenceMapper mapStructMapper, ReactiveMongoTemplate mongoTemplate) {
        super(repository, objectMapper, mapStructMapper::toDomain);
        this.mapStructMapper = mapStructMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Shipment> save(Shipment shipment) {
        ShipmentDocument document = mapStructMapper.toDocument(shipment);

        return repository.save(document)
                .map(mapStructMapper::toDomain);
    }

    @Override
    public Mono<Shipment> findById(String id) {
        return repository.findById(id)
                .map(mapStructMapper::toDomain);
    }

    @Override
    public Mono<PageResult<Shipment>> findAll(int page, int size) {
        // Configuramos qué página y cuántos elementos queremos
        Query query = new Query().with(PageRequest.of(page, size));

        // Tarea A: Contar el total de elementos en la base de datos
        Mono<Long> countMono = mongoTemplate.count(new Query(), ShipmentDocument.class);

        // Tarea B: Buscar solo los elementos de esta página y mapearlos al dominio
        Mono<List<Shipment>> listMono = mongoTemplate.find(query, ShipmentDocument.class)
                .map(mapStructMapper::toDomain)
                .collectList();

        // Mono.zip ejecuta la Tarea A y Tarea B al mismo tiempo para mayor velocidad
        return Mono.zip(countMono, listMono)
                .map(tuple -> {
                    long totalElements = tuple.getT1(); // Resultado del count
                    List<Shipment> data = tuple.getT2(); // Resultado de la lista

                    // Calculamos el total de páginas
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    // Construimos y retornamos el resultado paginado
                    return PageResult.<Shipment>builder()
                            .data(data)
                            .total(totalElements)
                            .page(page)
                            .size(size)
                            .totalPages(totalPages)
                            .build();
                });
    }
}
