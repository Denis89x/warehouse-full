package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;

@Data
public class OrderCompositionRequest {

    private Long productId;

    private Integer quantity;
}