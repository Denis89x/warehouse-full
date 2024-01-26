package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductTypeRequest;
import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;

import java.util.List;

public interface ProductTypeCRUDService {
    void saveProductType(ProductTypeRequest productTypeRequest);

    ProductTypeResponse fetchProductType(Long productTypeId);

    List<ProductTypeResponse> fetchAllProductTypes();

    void editProductType(Long productTypeId, ProductTypeRequest productTypeRequest);

    void deleteProductType(Long productTypeId);
}
