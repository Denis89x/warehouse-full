package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreRequest {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 3, max = 30, message = "Name should be 3 - 30 symbols size")
    private String name;
}
