package co.com.bancolombia.model.shipment;
import co.com.bancolombia.model.common.MockDataGenerator;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Builder(toBuilder = true)
public class Shipment {
    private final String id;
    private final String trackingNumber;
    private final LocalDate eta;
    private final String origin;
    private final String originTerminal;
    private final String destination;
    private final String destinationTerminal;
    private final String customer;
    private final String carrierName;
    private final String carrierService;
    private ShipmentStatus status;
    private final CargoDetail cargo;

    public Shipment(String id, String origin, String destination, String customer, CargoDetail cargoIncoming) {
        if (id == null || id.length() < 4) {
            throw new IllegalArgumentException("The ID is invalid for generating a tracking number");
        }
        this.id = id;
        this.trackingNumber = "#SHP-" + id.substring(0, 4).toUpperCase();
        this.eta = LocalDate.now().plusDays(ThreadLocalRandom.current().nextLong(3, 8));

        this.origin = origin;
        this.destination = destination;
        this.customer = customer;
        this.cargo = (cargoIncoming != null) ? cargoIncoming : MockDataGenerator.getRandomCargo();

        this.originTerminal = MockDataGenerator.getRandomTerminal();
        this.destinationTerminal = MockDataGenerator.getRandomTerminal();

        String[] carrierData = MockDataGenerator.getRandomCarrier();
        this.carrierName = carrierData[0];
        this.carrierService = carrierData[1];
        this.status = ShipmentStatus.IN_TRANSIT;
    }

    public Shipment(String id, String trackingNumber, LocalDate eta, String origin, String originTerminal, String destination, String destinationTerminal, String customer, String carrierName, String carrierService, ShipmentStatus status, CargoDetail cargo) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.eta = eta;
        this.origin = origin;
        this.originTerminal = originTerminal;
        this.destination = destination;
        this.destinationTerminal = destinationTerminal;
        this.customer = customer;
        this.carrierName = carrierName;
        this.carrierService = carrierService;
        this.status = status;
        this.cargo = cargo;
    }

    public void deliver() {
        if (status != ShipmentStatus.IN_TRANSIT) {
            throw new IllegalStateException("Only shipments in transit can be delivered");
        }
        this.status = ShipmentStatus.DELIVERED;
    }

    public void markIncident() {
        if (status == ShipmentStatus.DELIVERED) {
            throw new IllegalStateException("Delivered shipment cannot have incidents");
        }
        this.status = ShipmentStatus.INCIDENT;
    }
}


