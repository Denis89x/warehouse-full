package dev.lebenkov.warehouse.storage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCompositionResponse {

    private String productName;

    private Integer quantity;
}