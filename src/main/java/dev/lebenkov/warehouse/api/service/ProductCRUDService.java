package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductRequest;
import dev.lebenkov.warehouse.storage.dto.ProductResponse;

import java.util.List;

public interface ProductCRUDService {
    void saveProduct(ProductRequest productRequest);

    ProductResponse fetchProduct(Long productId);

    List<ProductResponse> fetchAllProducts();

    void editProduct(Long productId, ProductRequest productRequest);

    void deleteProduct(Long productId);
}
