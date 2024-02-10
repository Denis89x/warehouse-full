package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.DateFilterRequest;
import dev.lebenkov.warehouse.storage.dto.OrderCompositionResponse;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;
import dev.lebenkov.warehouse.storage.model.Order;
import dev.lebenkov.warehouse.storage.model.OrderComposition;
import dev.lebenkov.warehouse.storage.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    OrderRepository orderRepository;

    @Override
    public List<OrderResponse> findSimilarOrder(String field) {
        return orderRepository.findSimilarOrder(field).stream().map(this::convertToOrderResponse).toList();
    }

    @Override
    public List<OrderResponse> findOrdersByDateRange(DateFilterRequest dateFilterRequest) {
        return orderRepository.findByDateRange(dateFilterRequest.getStartDate(), dateFilterRequest.getEndDate()).stream()
                .map(this::convertToOrderResponse).toList();
    }

    private OrderResponse convertToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderType(order.getOrderType())
                .orderDate(order.getDate())
                .amount(order.getAmount())
                .storeName(order.getStore().getName())
                .supplierTitle(order.getSupplier().getTitle())
                .orderCompositionResponses(order.getOrderCompositions().stream().map(this::convertToOrderCompositionResponse).toList())
                .build();
    }

    private OrderCompositionResponse convertToOrderCompositionResponse(OrderComposition orderComposition) {
        return OrderCompositionResponse.builder()
                .productName(orderComposition.getProduct().getTitle())
                .quantity(orderComposition.getQuantity())
                .build();
    }
}
