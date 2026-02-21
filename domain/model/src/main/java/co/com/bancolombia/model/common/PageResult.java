package co.com.bancolombia.model.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResult<T> {
    private List<T> data;
    private long total;
    private int page;
    private int size;
    private int totalPages;
}