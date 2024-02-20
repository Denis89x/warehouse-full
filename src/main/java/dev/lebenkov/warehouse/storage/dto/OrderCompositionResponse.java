package dev.lebenkov.warehouse.storage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderCompositionResponse {

    private String productName;

    private Integer quantity;

    @Override
    public String toString() {
        return productName + " : " + quantity;
    }
}