package co.com.bancolombia.mongo;

import co.com.bancolombia.mongo.documents.ShipmentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBRepository extends ReactiveMongoRepository<ShipmentDocument, String>, ReactiveQueryByExampleExecutor<ShipmentDocument> {}
