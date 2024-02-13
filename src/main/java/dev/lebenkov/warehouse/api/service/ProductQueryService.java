package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductResponse;

import java.time.LocalDate;
import java.util.List;

public interface ProductQueryService {
    List<ProductResponse> findSimilarProducts(String field);

    List<ProductResponse> findProductsByDateRange(LocalDate startDate, LocalDate endDate);
}
