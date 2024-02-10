package dev.lebenkov.warehouse.storage.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderResponse {
    private Long orderId;

    private String orderType;

    private LocalDateTime orderDate;

    private Integer amount;

    private String storeName;

    private String supplierTitle;

    private List<OrderCompositionResponse> orderCompositionResponses;
}