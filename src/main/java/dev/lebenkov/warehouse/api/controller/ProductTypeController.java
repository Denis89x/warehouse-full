package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.ProductTypeCRUDService;
import dev.lebenkov.warehouse.storage.dto.ProductTypeRequest;
import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/types")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTypeController {

    ProductTypeCRUDService productTypeCRUDService;

    private final static String PRODUCT_TYPE_ID = "/{productTypeId}";

    @GetMapping(PRODUCT_TYPE_ID)
    public ResponseEntity<ProductTypeResponse> fetchProductType(@PathVariable Long productTypeId) {
        return new ResponseEntity<>(productTypeCRUDService.fetchProductType(productTypeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> fetchAllProductTypes() {
        return new ResponseEntity<>(productTypeCRUDService.fetchAllProductTypes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createProductType(@RequestBody ProductTypeRequest productTypeRequest) {
        productTypeCRUDService.saveProductType(productTypeRequest);
        return new ResponseEntity<>("Product type was successfully added", HttpStatus.CREATED);
    }

    @PatchMapping(PRODUCT_TYPE_ID)
    public ResponseEntity<String> updateProductType(@PathVariable Long productTypeId, @RequestBody @Valid ProductTypeRequest productTypeRequest) {
        productTypeCRUDService.editProductType(productTypeId, productTypeRequest);
        return new ResponseEntity<>("Product type was successfully changed", HttpStatus.OK);
    }

    @DeleteMapping(PRODUCT_TYPE_ID)
    public ResponseEntity<String> deleteProductType(@PathVariable Long productTypeId) {
        productTypeCRUDService.deleteProductType(productTypeId);
        return new ResponseEntity<>("Product type was successfully deleted", HttpStatus.OK);
    }
}
