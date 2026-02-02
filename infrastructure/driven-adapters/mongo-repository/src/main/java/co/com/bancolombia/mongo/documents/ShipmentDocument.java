package co.com.bancolombia.mongo.documents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipments")
public class ShipmentDocument {

    @Id
    private String id;
    private String trackingNumber;
    private LocalDate eta;
    private String origin;
    private String destination;
    private String customer;
    private String status;
    private String originTerminal;
    private String destinationTerminal;
    private String carrierName;
    private String carrierService;
    private CargoDocument cargo;
    private List<DocumentFile> documents;
}