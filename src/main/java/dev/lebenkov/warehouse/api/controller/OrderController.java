package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.OrderCRUDService;
import dev.lebenkov.warehouse.api.service.OrderQueryService;
import dev.lebenkov.warehouse.storage.dto.DateFilterRequest;
import dev.lebenkov.warehouse.storage.dto.OrderRequest;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderCRUDService orderCRUDService;
    OrderQueryService orderQueryService;

    private final static String ORDER_ID = "/{orderId}";
    private final static String ORDER_FIELD = "/search/{orderField}";
    private final static String ORDER_FILTER = "/filter";

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        orderCRUDService.saveOrder(orderRequest);
        return new ResponseEntity<>("Order was successfully saved", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> fetchAllOrders() {
        return new ResponseEntity<>(orderCRUDService.fetchAllOrders(), HttpStatus.OK);
    }

    @GetMapping(ORDER_ID)
    public ResponseEntity<OrderResponse> fetchOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderCRUDService.fetchOrder(orderId), HttpStatus.OK);
    }

    @GetMapping(ORDER_FIELD)
    public ResponseEntity<List<OrderResponse>> findSimilarOrder(@PathVariable String orderField) {
        return new ResponseEntity<>(orderQueryService.findSimilarOrder(orderField), HttpStatus.OK);
    }

    @GetMapping(ORDER_FILTER)
    public ResponseEntity<List<OrderResponse>> findOrdersByDateRange(@RequestBody DateFilterRequest dateFilterRequest) {
        return new ResponseEntity<>(orderQueryService.findOrdersByDateRange(dateFilterRequest), HttpStatus.OK);
    }

    @DeleteMapping(ORDER_ID)
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderCRUDService.deleteOrder(orderId);
        return new ResponseEntity<>("Order with " + orderId + " was successfully deleted", HttpStatus.OK);
    }
}