package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class ProductResponse {

    private Long productId;

    private String title;

    private LocalDateTime date;

    private Integer presence;

    private Integer cost;

    private String description;

    private ProductTypeResponse productTypeResponse;
}
