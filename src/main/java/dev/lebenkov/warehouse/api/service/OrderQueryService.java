package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.DateFilterRequest;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;

import java.util.List;

public interface OrderQueryService {
    List<OrderResponse> findSimilarOrder(String field);

    List<OrderResponse> findOrdersByDateRange(DateFilterRequest dateFilterRequest);
}
