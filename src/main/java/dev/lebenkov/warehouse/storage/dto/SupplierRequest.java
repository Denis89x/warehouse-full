package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = "Title should not be empty")
    @Size(min = 5, max = 50, message = "Title should be 5 - 50 symbols size")
    private String title;

    @NotBlank(message = "Surname should not be empty")
    @Size(min = 5, max = 50, message = "Surname should be 5 - 50 symbols size")
    private String surname;

    @NotBlank(message = "Address should not be empty")
    @Size(min = 7, max = 50, message = "Address should be 7 - 50 symbols size")
    private String address;

    @NotBlank(message = "Phone number should not be empty")
    @Size(min = 7, max = 20, message = "Phone number should be 7 - 20 symbols size")
    private String phoneNumber;
}
