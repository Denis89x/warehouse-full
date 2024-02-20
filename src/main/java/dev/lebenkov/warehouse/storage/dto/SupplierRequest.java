package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = "Title must not be empty")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 50 characters long")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9\\s\\-]+$", message = "Incorrect title")
    private String title;

    @NotBlank(message = "Surname must not be empty")
    @Size(min = 5, max = 50, message = "Surname must be between 3 and 50 characters long")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "The surname must contain only letters")
    private String surname;

    @NotBlank(message = "Address must not be empty")
    @Size(min = 7, max = 50, message = "Address must be between 3 and 50 characters long")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9\\s\\-]+$", message = "Incorrect address")
    private String address;

    @NotBlank(message = "Phone number must not be empty")
    @Size(min = 7, max = 20, message = "Phone number must be between 7 and 20 characters long")
    @Pattern(regexp = "^\\+375(29|25|44)\\d{7}$", message = "Incorrect phone number")
    private String phoneNumber;
}
