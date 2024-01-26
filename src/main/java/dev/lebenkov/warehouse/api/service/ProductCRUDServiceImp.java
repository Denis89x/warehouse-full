package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.ProductNotFoundException;
import dev.lebenkov.warehouse.api.util.exception.ProductTypeNotFoundException;
import dev.lebenkov.warehouse.storage.dto.ProductRequest;
import dev.lebenkov.warehouse.storage.dto.ProductResponse;
import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
import dev.lebenkov.warehouse.storage.model.Product;
import dev.lebenkov.warehouse.storage.model.ProductType;
import dev.lebenkov.warehouse.storage.repository.ProductRepository;
import dev.lebenkov.warehouse.storage.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCRUDServiceImp implements ProductCRUDService {

    ProductRepository productRepository;
    ProductTypeRepository productTypeRepository;

    ProductTypeCRUDService productTypeCRUDService;

    ModelMapper modelMapper;

    @Override
    public void saveProduct(ProductRequest productRequest) {
        Product product = convertToProduct(productRequest);

        product.setPresence(0);

        try {
            productRepository.save(product);
        } catch (JpaObjectRetrievalFailureException e) {
            throw new ProductTypeNotFoundException("Product type with " + productRequest.getProductTypeId() + " id not found");
        }
    }

    @Override
    public ProductResponse fetchProduct(Long productId) {
        return convertToProductResponse(findProductById(productId));
    }

    @Override
    public List<ProductResponse> fetchAllProducts() {
        return productRepository.findAll().stream().map(this::convertToProductResponse).toList();
    }

    @Override
    @Transactional
    public void editProduct(Long productId, ProductRequest productRequest) {
        Product product = findProductById(productId);

        product.setCost(productRequest.getCost());
        product.setTitle(productRequest.getTitle());
        product.setDate(productRequest.getDate());
        product.setDescription(productRequest.getDescription());

        ProductType productType = productTypeRepository.findById(productRequest.getProductTypeId())
                .orElseThrow(() -> new ProductTypeNotFoundException("Product type with " + productRequest.getProductTypeId() + " id not found"));
        product.setProductType(productType);

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    private Product convertToProduct(ProductRequest productRequest) {
        return modelMapper.map(productRequest, Product.class);
    }

    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);

        ProductTypeResponse productTypeResponse = productTypeCRUDService.fetchProductType(product.getProductType().getProductTypeId());
        productResponse.setProductTypeResponse(productTypeResponse);

        return productResponse;
    }
}