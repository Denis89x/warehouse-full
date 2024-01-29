package dev.lebenkov.warehouse.storage.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderResponse {
    private String orderType;

    private LocalDateTime orderDate;

    private Integer amount;

    private String storeName;

    private List<OrderCompositionResponse> orderCompositionResponses;
}