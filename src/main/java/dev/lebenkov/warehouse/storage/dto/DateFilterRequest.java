package dev.lebenkov.warehouse.storage.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateFilterRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
