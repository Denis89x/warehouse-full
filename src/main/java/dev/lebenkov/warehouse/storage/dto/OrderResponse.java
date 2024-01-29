package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String orderType;

    private LocalDateTime orderDate;

    private Integer amount;

    private Long storeId;

    private List<OrderCompositionRequest> orderCompositionRequestList;
}