package co.com.bancolombia.mongo.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoDocument {
    private String packageType;  // "Wooden Crate"
    private String quantity;     // "2 Crates"
    private String dimensions;   // "150 x 150 x 120 cm"
    private String volume;       // "5.4 CBM"
    private String commodity;    // "Auto Parts"
    private Boolean stackable;   // true
    private String hsCode;       // "8708.99"
    private Double weight;       // 340.0 (Numérico para cálculos)
}