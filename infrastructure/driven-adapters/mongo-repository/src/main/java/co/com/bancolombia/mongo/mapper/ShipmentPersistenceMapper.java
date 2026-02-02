package co.com.bancolombia.mongo.mapper;

import co.com.bancolombia.model.shipment.Shipment;
import co.com.bancolombia.mongo.documents.ShipmentDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentPersistenceMapper {

    // Dominio -> Mongo
    ShipmentDocument toDocument(Shipment shipment);

    // Mongo -> Dominio
    Shipment toDomain(ShipmentDocument document);
}
