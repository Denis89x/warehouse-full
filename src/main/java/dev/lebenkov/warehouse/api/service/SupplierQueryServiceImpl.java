package dev.lebenkov.warehouse.api.service;

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
public class SupplierQueryServiceImpl implements SupplierQueryService {

    SupplierRepository supplierRepository;

    ModelMapper modelMapper;

    @Override
    public List<SupplierResponse> findSimilarSupplier(String field) {
        return supplierRepository.findSimilarSupplier(field).stream().map(this::convertToSupplierResponse).toList();
    }

    private SupplierResponse convertToSupplierResponse(Supplier supplier) {
        return modelMapper.map(supplier, SupplierResponse.class);
    }
}
