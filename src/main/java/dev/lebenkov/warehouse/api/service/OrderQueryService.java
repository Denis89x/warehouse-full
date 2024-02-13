package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.OrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderQueryService {
    List<OrderResponse> findSimilarOrder(String field);

    List<OrderResponse> findOrdersByDateRange(LocalDate startDate, LocalDate endDate);
}
