package dev.lebenkov.warehouse.storage.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductTypeRequest {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 3, max = 50, message = "Name should be 3 - 50 symbols size")
    @Column(name = "name")
    private String name;
}
