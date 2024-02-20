package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequest {

    @Pattern(regexp = "^(Поступление|Выбытие)$", message = "orderType must be 'Поступление' or 'Выбытие'")
    private String orderType;

    private LocalDate orderDate;

    private Long storeId;

    private Long supplierId;

    private List<OrderCompositionRequest> orderCompositionRequestList;
}