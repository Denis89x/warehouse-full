package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.SupplierResponse;

import java.util.List;

public interface SupplierQueryService {
    List<SupplierResponse> findSimilarSupplier(String field);
}
