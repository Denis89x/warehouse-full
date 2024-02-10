package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;

import java.util.List;

public interface ProductTypeQueryService {
    List<ProductTypeResponse> findSimilarProductTypes(String field);
}
