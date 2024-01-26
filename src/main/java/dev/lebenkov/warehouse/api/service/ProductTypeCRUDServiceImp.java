package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.ProductTypeNotFoundException;
import dev.lebenkov.warehouse.storage.dto.ProductTypeRequest;
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
public class ProductTypeCRUDServiceImp implements ProductTypeCRUDService {

    ProductTypeRepository productTypeRepository;

    ModelMapper modelMapper;

    @Override
    public void saveProductType(ProductTypeRequest productTypeRequest) {
        productTypeRepository.save(convertToProductType(productTypeRequest));
    }

    @Override
    public ProductTypeResponse fetchProductType(Long productTypeId) {
        return convertToProductTypeResponse(findProductTypeById(productTypeId));
    }

    @Override
    public List<ProductTypeResponse> fetchAllProductTypes() {
        return productTypeRepository.findAll().stream()
                .map(this::convertToProductTypeResponse).toList();
    }

    @Override
    public void editProductType(Long productTypeId, ProductTypeRequest productTypeRequest) {
        ProductType productType = findProductTypeById(productTypeId);

        productType.setName(productTypeRequest.getName());
        productTypeRepository.save(productType);
    }

    @Override
    public void deleteProductType(Long productTypeId) {
        productTypeRepository.deleteById(productTypeId);
    }

    private ProductType convertToProductType(ProductTypeRequest productTypeRequest) {
        return modelMapper.map(productTypeRequest, ProductType.class);
    }

    private ProductTypeResponse convertToProductTypeResponse(ProductType productType) {
        return modelMapper.map(productType, ProductTypeResponse.class);
    }

    private ProductType findProductTypeById(Long productTypeId) {
        return productTypeRepository.findById(productTypeId).orElseThrow(() ->
                new ProductTypeNotFoundException("Product with id " + productTypeId + " not found"));
    }
}