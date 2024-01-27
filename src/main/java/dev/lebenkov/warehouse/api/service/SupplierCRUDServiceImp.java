package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.SupplierNotFoundException;
import dev.lebenkov.warehouse.storage.dto.SupplierRequest;
import dev.lebenkov.warehouse.storage.dto.SupplierResponse;
import dev.lebenkov.warehouse.storage.model.Supplier;
import dev.lebenkov.warehouse.storage.repository.SupplierRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierCRUDServiceImp implements SupplierCRUDService {

    SupplierRepository supplierRepository;

    ModelMapper modelMapper;

    @Override
    public void saveSupplier(SupplierRequest supplierRequest) {
        supplierRepository.save(convertToSupplier(supplierRequest));
    }

    @Override
    public SupplierResponse fetchSupplier(Long supplierId) {
        return convertToSupplierResponse(findSupplierById(supplierId));
    }

    @Override
    public List<SupplierResponse> fetchAllSuppliers() {
        return supplierRepository.findAll().stream().map(this::convertToSupplierResponse).toList();
    }

    @Override
    public void editSupplier(Long supplierId, SupplierRequest supplierRequest) {
        Supplier supplier = findSupplierById(supplierId);

        supplier.setTitle(supplierRequest.getTitle());
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setSurname(supplierRequest.getSurname());
        supplier.setPhoneNumber(supplierRequest.getPhoneNumber());

        supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        supplierRepository.deleteById(supplierId);
    }

    private Supplier findSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId).orElseThrow(() ->
                new SupplierNotFoundException("Supplier with " + supplierId + " not found"));
    }

    private SupplierResponse convertToSupplierResponse(Supplier supplier) {
        return modelMapper.map(supplier, SupplierResponse.class);
    }

    private Supplier convertToSupplier(SupplierRequest supplierRequest) {
        return modelMapper.map(supplierRequest, Supplier.class);
    }
}