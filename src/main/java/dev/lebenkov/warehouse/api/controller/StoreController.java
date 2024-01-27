package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.StoreCRUDService;
import dev.lebenkov.warehouse.storage.dto.StoreRequest;
import dev.lebenkov.warehouse.storage.dto.StoreResponse;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreController {

    StoreCRUDService storeCRUDService;

    private final static String STORE_ID = "/{storeId}";

    @GetMapping(STORE_ID)
    public ResponseEntity<StoreResponse> fetchStore(@PathVariable Long storeId) {
        return new ResponseEntity<>(storeCRUDService.fetchStore(storeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> fetchAllStore() {
        return new ResponseEntity<>(storeCRUDService.fetchAllStores(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createStore(@RequestBody StoreRequest storeRequest) {
        storeCRUDService.saveStore(storeRequest);
        return new ResponseEntity<>("Product  was successfully added", HttpStatus.CREATED);
    }

    @PatchMapping(STORE_ID)
    public ResponseEntity<String> updateStore(@PathVariable Long storeId, @RequestBody @Valid StoreRequest storeRequest) {
        storeCRUDService.editStore(storeId, storeRequest);
        return new ResponseEntity<>("Product  was successfully changed", HttpStatus.OK);
    }

    @DeleteMapping(STORE_ID)
    public ResponseEntity<String> deleteStore(@PathVariable Long storeId) {
        storeCRUDService.deleteStore(storeId);
        return new ResponseEntity<>("Product with " + storeId + " id was successfully deleted", HttpStatus.OK);
    }
}