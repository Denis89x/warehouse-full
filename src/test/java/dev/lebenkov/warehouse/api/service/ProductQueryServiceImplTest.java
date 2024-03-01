package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductResponse;
import dev.lebenkov.warehouse.storage.model.Product;
import dev.lebenkov.warehouse.storage.model.ProductType;
import dev.lebenkov.warehouse.storage.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ProductQueryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductQueryServiceImpl productQueryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldFindSimilarProducts() {
        when(productRepository.findByCustomQuery("someField")).thenReturn(Arrays.asList(Product.builder()
                .productType(ProductType.builder().build())
                .build()));

        ProductResponse productResponse = new ProductResponse();
        when(modelMapper.map(any(), eq(ProductResponse.class))).thenReturn(productResponse);

        List<ProductResponse> result = productQueryService.findSimilarProducts("someField");

        assertEquals(1, result.size());

        assertEquals(productResponse, result.get(0));
    }

    @Test
    void shouldFindProductsByDateRange() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        when(productRepository.findByDateRange(startDate, endDate)).thenReturn(Arrays.asList(Product.builder()
                .productType(ProductType.builder().build())
                .build()));

        ProductResponse productResponse = new ProductResponse();
        when(modelMapper.map(any(), eq(ProductResponse.class))).thenReturn(productResponse);

        List<ProductResponse> result = productQueryService.findProductsByDateRange(startDate, endDate);

        assertEquals(1, result.size());

        assertEquals(productResponse, result.get(0));
    }
}