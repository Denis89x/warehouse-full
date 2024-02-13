package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.ProductCRUDService;
import dev.lebenkov.warehouse.api.service.ProductQueryService;
import dev.lebenkov.warehouse.storage.dto.ProductRequest;
import dev.lebenkov.warehouse.storage.dto.ProductResponse;
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

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductCRUDService productCRUDService;
    ProductQueryService productQueryService;

    private final static String PRODUCT_ID = "/{productId}";
    private final static String PRODUCT_FIELD = "/search/{productField}";
    private final static String PRODUCT_FILTRATE = "/filter";

    @GetMapping(PRODUCT_ID)
    public ResponseEntity<ProductResponse> fetchProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productCRUDService.fetchProduct(productId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> fetchAllProducts() {
        return new ResponseEntity<>(productCRUDService.fetchAllProducts(), HttpStatus.OK);
    }

    @GetMapping(PRODUCT_FIELD)
    public ResponseEntity<List<ProductResponse>> findSimilarProducts(@PathVariable String productField) {
        return new ResponseEntity<>(productQueryService.findSimilarProducts(productField), HttpStatus.OK);
    }

    @GetMapping(PRODUCT_FILTRATE)
    public ResponseEntity<List<ProductResponse>> findProductsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return new ResponseEntity<>(productQueryService.findProductsByDateRange(startDate, endDate), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        productCRUDService.saveProduct(productRequest);
        return new ResponseEntity<>("Product  was successfully added", HttpStatus.CREATED);
    }

    @PatchMapping(PRODUCT_ID)
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
        productCRUDService.editProduct(productId, productRequest);
        return new ResponseEntity<>("Product  was successfully changed", HttpStatus.OK);
    }

    @DeleteMapping(PRODUCT_ID)
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productCRUDService.deleteProduct(productId);
        return new ResponseEntity<>("Product with " + productId + " id was successfully deleted", HttpStatus.OK);
    }
}
