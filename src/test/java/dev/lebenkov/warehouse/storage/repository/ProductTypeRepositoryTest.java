package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {"spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=true"})
public class ProductTypeRepositoryTest {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Test
    public void ProductTypeRepository_FindSimilarProductTypes_ReturnSimilarProductTypes() {
        // Arrange
        ProductType productType = ProductType.builder()
                .name("test1")
                .build();

        ProductType productType1 = ProductType.builder()
                .name("test2")
                .build();


        ProductType productType2 = ProductType.builder()
                .name("product type")
                .build();

        // Act
        productTypeRepository.save(productType);
        productTypeRepository.save(productType1);
        productTypeRepository.save(productType2);

        List<ProductType> productTypes = productTypeRepository.findSimilarProductTypes("test");

        // Assert
        Assertions.assertThat(productTypes).isNotNull();
        Assertions.assertThat(productTypes.size()).isEqualTo(2);
    }
}