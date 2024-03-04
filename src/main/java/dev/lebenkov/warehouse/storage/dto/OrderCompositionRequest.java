package dev.lebenkov.warehouse.storage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCompositionRequest {

    private Long productId;

    private Integer quantity;
}