package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.ProductNotFoundException;
import dev.lebenkov.warehouse.api.util.exception.StoreNotFoundException;
import dev.lebenkov.warehouse.api.util.exception.SupplierNotFoundException;
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
        Order order = Order.builder()
                .orderType(orderRequest.getOrderType())
                .account(fetchAccount())
                .amount(0) //
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
    public void editOrder(Long orderId) {

    }

    @Override
    public void deleteOrder(Long orderId) {

    }

    @Override
    public OrderResponse fetchOrder(Long orderId) {
        return null;
    }

    @Override
    public List<OrderResponse> fetchAllOrders() {
        return null;
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
}