package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.OrderResponse;

import java.util.List;

public interface OrderQueryService {
    List<OrderResponse> findSimilarOrder(String field);
}
