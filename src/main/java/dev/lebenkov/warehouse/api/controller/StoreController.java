package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.StoreCRUDService;
import dev.lebenkov.warehouse.api.service.StoreQueryService;
import dev.lebenkov.warehouse.storage.dto.StoreRequest;
import dev.lebenkov.warehouse.storage.dto.StoreResponse;
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
@RequestMapping("/api/v1/stores")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreController {

    StoreCRUDService storeCRUDService;
    StoreQueryService storeQueryService;

    private final static String STORE_ID = "/{storeId}";
    private final static String STORE_FIELD = "/search/{storeField}";

    @GetMapping(STORE_ID)
    public ResponseEntity<StoreResponse> fetchStore(@PathVariable Long storeId) {
        return new ResponseEntity<>(storeCRUDService.fetchStore(storeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> fetchAllStore() {
        return new ResponseEntity<>(storeCRUDService.fetchAllStores(), HttpStatus.OK);
    }

    @GetMapping(STORE_FIELD)
    public ResponseEntity<List<StoreResponse>> findSimilarStore(@PathVariable String storeField) {
        return new ResponseEntity<>(storeQueryService.findSimilarStore(storeField), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createStore(@RequestBody @Valid StoreRequest storeRequest) {
        storeCRUDService.saveStore(storeRequest);
        return new ResponseEntity<>("Store  was successfully added", HttpStatus.CREATED);
    }

    @PatchMapping(STORE_ID)
    public ResponseEntity<String> updateStore(@PathVariable Long storeId, @RequestBody @Valid StoreRequest storeRequest) {
        storeCRUDService.editStore(storeId, storeRequest);
        return new ResponseEntity<>("Store was successfully changed", HttpStatus.OK);
    }

    @DeleteMapping(STORE_ID)
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId) {
        storeCRUDService.deleteStore(storeId);
        return new ResponseEntity<>("Store with " + storeId + " id was successfully deleted", HttpStatus.OK);
    }
}