package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductResponse;

import java.util.List;

public interface ProductQueryService {
    List<ProductResponse> findSimilarProducts(String field);
}
