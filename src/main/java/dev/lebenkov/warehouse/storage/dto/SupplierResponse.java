package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;

@Data
public class SupplierResponse {

    private Long supplierId;

    private String title;

    private String surname;

    private String address;

    private String phoneNumber;
}