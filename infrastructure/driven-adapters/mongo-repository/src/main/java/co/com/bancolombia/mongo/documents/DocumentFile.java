package co.com.bancolombia.mongo.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFile {
    private String name;    // "Commercial Invoice"
    private String format;  // "PDF"
    private String size;    // "1.2 MB"
}