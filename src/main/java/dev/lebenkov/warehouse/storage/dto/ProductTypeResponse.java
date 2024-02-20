package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductTypeResponse {

    private Long productTypeId;

    private String name;
}
