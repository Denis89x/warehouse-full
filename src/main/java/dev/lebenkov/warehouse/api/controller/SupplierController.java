package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.SupplierCRUDService;
import dev.lebenkov.warehouse.api.service.SupplierQueryService;
import dev.lebenkov.warehouse.storage.dto.SupplierRequest;
import dev.lebenkov.warehouse.storage.dto.SupplierResponse;
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
@RequestMapping("/api/v1/suppliers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController {

    SupplierCRUDService supplierCRUDService;
    SupplierQueryService supplierQueryService;

    private static final String SUPPLIER_ID = "/{supplierId}";
    private static final String SUPPLIER_FIELD = "/search/{supplierField}";

    @GetMapping(SUPPLIER_ID)
    public ResponseEntity<SupplierResponse> fetchSupplier(@PathVariable Long supplierId) {
        return new ResponseEntity<>(supplierCRUDService.fetchSupplier(supplierId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> fetchAllSupplier() {
        return new ResponseEntity<>(supplierCRUDService.fetchAllSuppliers(), HttpStatus.OK);
    }

    @GetMapping(SUPPLIER_FIELD)
    public ResponseEntity<List<SupplierResponse>> findSimilarSupplier(@PathVariable String supplierField) {
        return new ResponseEntity<>(supplierQueryService.findSimilarSupplier(supplierField), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createSupplier(@RequestBody @Valid SupplierRequest supplierRequest) {
        supplierCRUDService.saveSupplier(supplierRequest);
        return new ResponseEntity<>("Supplier was successfully added", HttpStatus.CREATED);
    }

    @PatchMapping(SUPPLIER_ID)
    public ResponseEntity<String> updateSupplier(@PathVariable Long supplierId, @RequestBody @Valid SupplierRequest supplierRequest) {
        supplierCRUDService.editSupplier(supplierId, supplierRequest);
        return new ResponseEntity<>("Supplier was successfully changed", HttpStatus.OK);
    }

    @DeleteMapping(SUPPLIER_ID)
    public ResponseEntity<String> deleteSupplier(@PathVariable Long supplierId) {
        supplierCRUDService.deleteSupplier(supplierId);
        return new ResponseEntity<>("Supplier with " + supplierId + " id was successfully deleted", HttpStatus.OK);
    }
}