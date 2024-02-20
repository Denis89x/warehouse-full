package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.StoreResponse;

import java.util.List;

public interface StoreQueryService {
    List<StoreResponse> findSimilarStore(String field);
}
