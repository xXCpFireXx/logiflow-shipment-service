package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.ShipmentRequest;
import co.com.bancolombia.api.dto.ShipmentResponse;
import co.com.bancolombia.model.shipment.Location;
import co.com.bancolombia.model.shipment.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShipmentRestMapper {

    // REQUEST -> DOMINIO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customer", source = "customerId")
    Shipment toDomain(ShipmentRequest request);

    // DOMINIO -> RESPONSE
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cargoDetails", source = "cargo")
    @Mapping(target = "details", source = "shipment", qualifiedByName = "buildUiDetails")
    @Mapping(target = "documents", expression = "java(mockDocuments())")
    ShipmentResponse toResponse(Shipment shipment);

    @Named("buildUiDetails")
    default ShipmentResponse.UiDetailsResponse buildUiDetails(Shipment s) {
        if (s == null) return null;

        return new ShipmentResponse.UiDetailsResponse(
                new ShipmentResponse.UiItem("Origin", formatLocation(s.getOrigin()), s.getOriginTerminal()),
                new ShipmentResponse.UiItem("Destination", formatLocation(s.getDestination()), s.getDestinationTerminal()),
                new ShipmentResponse.UiItem("Carrier", s.getCarrierName(), s.getCarrierService()),
                new ShipmentResponse.UiItem("Weight", s.getCargo().getWeight() + " kg", s.getCargo().getQuantity())
        );
    }

    default String formatLocation(Location loc) {
        if (loc == null) return "";
        return loc.getCity() + ", " + loc.getCountry();
    }

    default List<ShipmentResponse.DocumentResponse> mockDocuments() {
        return List.of(new ShipmentResponse.DocumentResponse("Invoice.pdf", "PDF", "1.2 MB"));
    }
}