package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductResponse;
import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
import dev.lebenkov.warehouse.storage.model.Product;
import dev.lebenkov.warehouse.storage.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    ProductRepository productRepository;

    ProductTypeCRUDService productTypeCRUDService;

    ModelMapper modelMapper;

    @Override
    public List<ProductResponse> findSimilarProducts(String field) {
        return productRepository.findByCustomQuery(field).stream().map(this::convertToProductResponse).toList();
    }

    @Override
    public List<ProductResponse> findProductsByDateRange(LocalDate startDate, LocalDate endDate) {
        return productRepository.findByDateRange(startDate, endDate).stream().map(this::convertToProductResponse).toList();
    }

    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);

        ProductTypeResponse productTypeResponse = productTypeCRUDService.fetchProductType(product.getProductType().getProductTypeId());
        productResponse.setProductTypeResponse(productTypeResponse);

        return productResponse;
    }
}
