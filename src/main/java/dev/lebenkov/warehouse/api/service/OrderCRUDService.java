package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.OrderRequest;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;

import java.util.List;

public interface OrderCRUDService {
    void saveOrder(OrderRequest orderRequest);

    void editOrder(Long orderId);

    void deleteOrder(Long orderId);

    OrderResponse fetchOrder(Long orderId);

    List<OrderResponse> fetchAllOrders();
}