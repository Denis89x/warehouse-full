package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.config.SecurityConfig;
import dev.lebenkov.warehouse.storage.dto.OrderCompositionRequest;
import dev.lebenkov.warehouse.storage.dto.OrderRequest;
import dev.lebenkov.warehouse.storage.model.Account;
import dev.lebenkov.warehouse.storage.model.Order;
import dev.lebenkov.warehouse.storage.model.OrderComposition;
import dev.lebenkov.warehouse.storage.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@Import(SecurityConfig.class)
class OrderCRUDServiceImpTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCompositionRepository orderCompositionRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    private OrderCRUDService orderCRUDService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderCRUDService = new OrderCRUDServiceImp(
                storeRepository, orderRepository, accountRepository,
                productRepository, supplierRepository, orderCompositionRepository
        );
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldSaveOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderType("Поступление");
        orderRequest.setOrderDate(LocalDate.now());
        orderRequest.setStoreId(1L);
        orderRequest.setSupplierId(2L);
        orderRequest.setOrderCompositionRequestList(Arrays.asList(
                OrderCompositionRequest.builder()
                        .productId(1L)
                        .quantity(10)
                        .build()));

        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(orderCompositionRepository.save(any(OrderComposition.class))).thenReturn(new OrderComposition());

        orderCRUDService.saveOrder(orderRequest);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderCompositionRepository, times(1)).save(any(OrderComposition.class));
    }

    @Test
    void shouldFetchOrderResponse() {
    }
}