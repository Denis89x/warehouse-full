package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
import dev.lebenkov.warehouse.storage.model.ProductType;
import dev.lebenkov.warehouse.storage.repository.ProductTypeRepository;
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
public class ProductTypeQueryServiceImpl implements ProductTypeQueryService {

    ProductTypeRepository productTypeRepository;

    ModelMapper modelMapper;

    @Override
    public List<ProductTypeResponse> findSimilarProductTypes(String field) {
        return productTypeRepository.findSimilarProductTypes(field).stream().map(this::convertToProductTypeResponse).toList();
    }

    private ProductTypeResponse convertToProductTypeResponse(ProductType productType) {
        return modelMapper.map(productType, ProductTypeResponse.class);
    }
}
