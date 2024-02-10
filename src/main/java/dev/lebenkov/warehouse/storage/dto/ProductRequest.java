package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ProductRequest {

    @NotBlank(message = "Title must not be empty")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters long")
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9\\s\\-]+$", message = "Incorrect title")
    private String title;

    @FutureOrPresent(message = "The date can't be earlier than today")
    private LocalDateTime date;

    @Max(value = 1000, message = "Cost cannot be more than 1000")
    @Min(value = 50, message = "Cost cannot be litter than 50")
    private Integer cost;

    private String description;

    private Long productTypeId;
}
