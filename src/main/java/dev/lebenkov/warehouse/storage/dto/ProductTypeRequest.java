package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductTypeRequest {

    @NotBlank(message = "Name must not be empty")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters long")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9\\s\\-]+$", message = "Incorrect Name")
    private String name;
}
