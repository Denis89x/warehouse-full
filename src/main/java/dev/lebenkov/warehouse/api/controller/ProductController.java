package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.ProductCRUDService;
import dev.lebenkov.warehouse.storage.dto.ProductRequest;
import dev.lebenkov.warehouse.storage.dto.ProductResponse;
import dev.lebenkov.warehouse.storage.dto.ProductTypeRequest;
import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductCRUDService productCRUDService;

    private final static String PRODUCT_ID = "/{productId}";

    @GetMapping(PRODUCT_ID)
    public ResponseEntity<ProductResponse> fetchProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productCRUDService.fetchProduct(productId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> fetchAllProducts() {
        return new ResponseEntity<>(productCRUDService.fetchAllProducts(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
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
