package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.*;
import dev.lebenkov.warehouse.storage.dto.OrderCompositionResponse;
import dev.lebenkov.warehouse.storage.dto.OrderRequest;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;
import dev.lebenkov.warehouse.storage.model.*;
import dev.lebenkov.warehouse.storage.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCRUDServiceImp implements OrderCRUDService {

    StoreRepository storeRepository;
    OrderRepository orderRepository;
    AccountRepository accountRepository;
    ProductRepository productRepository;
    SupplierRepository supplierRepository;
    OrderCompositionRepository orderCompositionRepository;

    @Override
    @Transactional
    public void saveOrder(OrderRequest orderRequest) {
        if (orderRequest.getOrderCompositionRequestList().isEmpty()) {
            throw new EmptyOrderCompositionException("Unable to create an order with an empty list of products");
        }

        Order order = Order.builder()
                .orderType(orderRequest.getOrderType())
                .account(fetchAccount())
                .amount(0)
                .date(orderRequest.getOrderDate())
                .store(findStoreById(orderRequest.getStoreId()))
                .supplier(findSupplierById(orderRequest.getSupplierId()))
                .build();

        orderRepository.save(order);

        orderRequest.getOrderCompositionRequestList().forEach(orderCompositionRequest -> {
            OrderComposition orderComposition = new OrderComposition();

            orderComposition.setProduct(findProductById(orderCompositionRequest.getProductId()));
            orderComposition.setOrder(order);
            orderComposition.setQuantity(orderCompositionRequest.getQuantity());

            orderCompositionRepository.save(orderComposition);
        });
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = findOrderById(orderId);

        order.getOrderCompositions().forEach(orderComposition -> orderCompositionRepository.deleteById(orderComposition.getOrderCompositionId()));

        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderResponse fetchOrder(Long orderId) {
        return convertToOrderResponse(findOrderById(orderId));
    }

    @Override
    public List<OrderResponse> fetchAllOrders() {
        return orderRepository.findAll().stream().map(this::convertToOrderResponse).toList();
    }

    private Account fetchAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Account not founded!"));
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("Store with " + storeId + " not found"));
    }

    private Supplier findSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId).orElseThrow(() ->
                new SupplierNotFoundException("Supplier with " + supplierId + " not found"));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException("Order with " + orderId + " id not found"));
    }

    private OrderResponse convertToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderType(order.getOrderType())
                .orderDate(order.getDate())
                .username(order.getAccount().getUsername())
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