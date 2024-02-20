package dev.lebenkov.warehouse.storage.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class OrderResponse {
    private Long orderId;

    private String orderType;

    private LocalDate orderDate;

    private Integer amount;

    private String username;

    private String storeName;

    private String supplierTitle;

    private List<OrderCompositionResponse> orderCompositionResponses;
}