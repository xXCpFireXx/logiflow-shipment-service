package co.com.bancolombia.model.common;

import co.com.bancolombia.model.shipment.CargoDetail;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MockDataGenerator {
    private static final List<CargoDetail> CARGOS = List.of(
            CargoDetail.builder().packageType("Wooden Crate").commodity("Auto Parts").weight(340.0).quantity("2 Crates").volume("5.4 CBM").dimensions("150x150x120").stackable(true).hsCode("8708.99").build(),
            CargoDetail.builder().packageType("Cardboard Box").commodity("Electronics").weight(120.5).quantity("50 Boxes").volume("1.2 CBM").dimensions("40x30x20").stackable(false).hsCode("8517.13").build()
    );

    private static final List<String[]> CARRIERS = List.of(
            new String[]{"DHL Express", "Service: Next Day"},
            new String[]{"FedEx Logistics", "Service: Ground Freight"},
            new String[]{"Maersk", "Service: Ocean Container"}
    );

    private static final List<String> TERMINALS = List.of("Warehouse B-12", "Logistics Hub P-05", "Cargo Terminal T-4");

    public static CargoDetail getRandomCargo() {
        return CARGOS.get(ThreadLocalRandom.current().nextInt(CARGOS.size()));
    }

    public static String[] getRandomCarrier() {
        return CARRIERS.get(ThreadLocalRandom.current().nextInt(CARRIERS.size()));
    }

    public static String getRandomTerminal() {
        return TERMINALS.get(ThreadLocalRandom.current().nextInt(TERMINALS.size()));
    }
}
