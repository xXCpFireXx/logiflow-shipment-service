package co.com.bancolombia.mongo;

import co.com.bancolombia.model.common.PageResult;
import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.model.shipment.gateways.ShipmentRepository;
import co.com.bancolombia.mongo.documents.ShipmentDocument;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.mapper.ShipmentPersistenceMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter
        extends AdapterOperations<Shipment, ShipmentDocument, String, MongoDBRepository>
        implements ShipmentRepository {

    private final ShipmentPersistenceMapper mapStructMapper;

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper objectMapper, ShipmentPersistenceMapper mapStructMapper) {
        super(repository, objectMapper, mapStructMapper::toDomain);
        this.mapStructMapper = mapStructMapper;
    }

    @Override
    public Mono<Shipment> save(Shipment shipment) {
        ShipmentDocument document = mapStructMapper.toDocument(shipment);

        return repository.save(document)
                .map(mapStructMapper::toDomain);
    }

    @Override
    public Mono<PageResult<Shipment>> findAll(int page, int size) {
        return null;
    }
}
