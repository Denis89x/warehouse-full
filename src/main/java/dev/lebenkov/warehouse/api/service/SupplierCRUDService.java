package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.SupplierRequest;
import dev.lebenkov.warehouse.storage.dto.SupplierResponse;

import java.util.List;

public interface SupplierCRUDService {
    void saveSupplier(SupplierRequest supplierRequest);

    SupplierResponse fetchSupplier(Long supplierId);

    List<SupplierResponse> fetchAllSuppliers();

    void editSupplier(Long supplierId, SupplierRequest supplierRequest);

    void deleteSupplier(Long supplierId);
}