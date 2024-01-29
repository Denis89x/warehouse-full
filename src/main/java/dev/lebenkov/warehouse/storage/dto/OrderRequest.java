package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private String orderType;

    private LocalDateTime orderDate;

    private Integer amount;

    private Long storeId;

    private Long supplierId;

    private List<OrderCompositionRequest> orderCompositionRequestList;
}
