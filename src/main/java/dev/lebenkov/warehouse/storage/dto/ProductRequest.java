package dev.lebenkov.warehouse.storage.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductRequest {

    @NotBlank(message = "Title should not be empty")
    @Size(min = 3, max = 50, message = "Title should be 3 - 50 symbols size")
    private String title;

    private LocalDateTime date;

    @Max(value = 1000, message = "Cost cannot be more then 1000")
    @Min(value = 0, message = "Cost cannot be litter then 0")
    private Integer cost;

    private String description;

    private Long productTypeId;
}
