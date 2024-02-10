package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {

    @Pattern(regexp = "^(Поступление|Выбытие)$", message = "orderType must be 'Поступление' or 'Выбытие'")
    private String orderType;

    private LocalDateTime orderDate;

    private Long storeId;

    private Long supplierId;

    private List<OrderCompositionRequest> orderCompositionRequestList;
}