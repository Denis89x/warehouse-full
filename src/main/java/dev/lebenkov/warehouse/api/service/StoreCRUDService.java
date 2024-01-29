package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.StoreRequest;
import dev.lebenkov.warehouse.storage.dto.StoreResponse;

import java.util.List;

public interface StoreCRUDService {

    void saveStore(StoreRequest storeRequest);

    StoreResponse fetchStore(Long storeId);

    List<StoreResponse> fetchAllStores();

    void editStore(Long storeId, StoreRequest storeRequest);

    void deleteStore(Long storeId);
}
