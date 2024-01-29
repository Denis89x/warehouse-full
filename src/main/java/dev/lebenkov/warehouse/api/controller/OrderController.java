package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.OrderCRUDService;
import dev.lebenkov.warehouse.storage.dto.OrderRequest;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderCRUDService orderCRUDService;

    private final static String ORDER_ID = "/{orderId}";

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
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

    @DeleteMapping(ORDER_ID)
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderCRUDService.deleteOrder(orderId);
        return new ResponseEntity<>("Order with " + orderId + " was successfully deleted", HttpStatus.OK);
    }
}