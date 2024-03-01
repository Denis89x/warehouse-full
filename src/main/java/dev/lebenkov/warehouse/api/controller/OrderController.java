package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.*;
import dev.lebenkov.warehouse.api.service.excel.OrderCompositionExcelService;
import dev.lebenkov.warehouse.api.service.excel.OrderExcelService;
import dev.lebenkov.warehouse.storage.dto.OrderRequest;
import dev.lebenkov.warehouse.storage.dto.OrderResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
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
    OrderExcelService orderExcelService;
    OrderCompositionExcelService orderCompositionExcelService;

    private final static String ORDER_ID = "/{orderId}";
    private final static String ORDER_FIELD = "/search/{orderField}";
    private final static String ORDER_FILTER = "/filter";
    private final static String FETCH_ORDERS_BY_USERNAME = "/user/{username}";
    private final static String FETCH_ORDERS_BY_SUPPLIER = "/supplier/{supplierId}";
    private final static String FETCH_ORDERS_BY_DATE_RANGE_EXCEL = "/excel";
    private final static String FETCH_ORDERS_BY_SUPPLIER_EXCEL = "/excel-supplier/{supplierId}";
    private final static String GENERATE_DELIVERY_NOTE_EXCEL = "/excel-delivery-note";
    private final static String FETCH_ORDER_EXCEL = "/excel-order/{orderId}";


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

    @GetMapping(FETCH_ORDERS_BY_USERNAME)
    public ResponseEntity<List<OrderResponse>> fetchOrdersByUsername(@PathVariable String username) {
        return new ResponseEntity<>(orderQueryService.findOrdersByUsername(username), HttpStatus.OK);
    }

    @GetMapping(FETCH_ORDERS_BY_SUPPLIER)
    public ResponseEntity<List<OrderResponse>> fetchOrdersBySupplierTitle(
            @PathVariable Long supplierId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return new ResponseEntity<>(orderQueryService.findOrderByTypeAndSupplier(supplierId, startDate, endDate), HttpStatus.OK);
    }

    @GetMapping(FETCH_ORDERS_BY_DATE_RANGE_EXCEL)
    public void writeExcel(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                           @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                           HttpServletResponse response) throws IOException {
        orderExcelService.generateExcelByDateRange(response, startDate, endDate);
    }

    @GetMapping(FETCH_ORDERS_BY_SUPPLIER_EXCEL)
    public void writeExcelBySupplier(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @PathVariable("supplierId") Long supplierId,
            HttpServletResponse response) throws IOException {
        orderExcelService.generateExcelBySupplierId(response, startDate, endDate, supplierId);
    }

    @GetMapping(FETCH_ORDER_EXCEL)
    public void writeExcelBySupplier(@PathVariable("orderId") Long orderId, HttpServletResponse response) throws IOException {
        orderExcelService.generateOrderExcel(response, orderId);
    }

    @GetMapping(GENERATE_DELIVERY_NOTE_EXCEL)
    public void writeExcelDeliveryNote(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletResponse response) throws IOException {
        orderCompositionExcelService.generateDeliveryNoteExcel(response, startDate, endDate);
    }

    @GetMapping(ORDER_FILTER)
    public ResponseEntity<List<OrderResponse>> findOrdersByDateRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return new ResponseEntity<>(orderQueryService.findOrdersByDateRange(startDate, endDate), HttpStatus.OK);
    }

    @DeleteMapping(ORDER_ID)
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderCRUDService.deleteOrder(orderId);
        return new ResponseEntity<>("Order with " + orderId + " was successfully deleted", HttpStatus.OK);
    }
}