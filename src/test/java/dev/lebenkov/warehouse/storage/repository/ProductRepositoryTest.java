package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Product;
import dev.lebenkov.warehouse.storage.model.ProductType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {"spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=true"})
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Test
    public void ProductRepository_FindByCustomQuery_ReturnSimilarProducts() {
        // Arrange
        ProductType productType = ProductType.builder()
                .name("product")
                .build();

        Product product1 = Product.builder()
                .title("title1")
                .date(LocalDate.now())
                .presence(0)
                .cost(1)
                .description("desc1")
                .productType(productType)
                .build();

        Product product2 = Product.builder()
                .title("title2")
                .date(LocalDate.now())
                .presence(0)
                .cost(1)
                .description("desc2")
                .productType(productType)
                .build();

        Product product3 = Product.builder()
                .title("product")
                .date(LocalDate.now())
                .presence(0)
                .cost(1)
                .description("description")
                .productType(productType)
                .build();

        // Act
        productTypeRepository.save(productType);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        List<Product> productsByTitle = productRepository.findByCustomQuery("title");
        List<Product> productsByDescription = productRepository.findByCustomQuery("desc");
        List<Product> productsByCost = productRepository.findByCustomQuery("1");
        List<Product> productsByPresence = productRepository.findByCustomQuery("0");

        // Assert
        Assertions.assertThat(productsByTitle).isNotNull();
        Assertions.assertThat(productsByDescription).isNotNull();
        Assertions.assertThat(productsByCost).isNotNull();
        Assertions.assertThat(productsByPresence).isNotNull();

        Assertions.assertThat(productsByTitle.size()).isEqualTo(2);
        Assertions.assertThat(productsByDescription.size()).isEqualTo(3);
        Assertions.assertThat(productsByCost.size()).isEqualTo(3);
        Assertions.assertThat(productsByPresence.size()).isEqualTo(3);
    }

    @Test
    public void ProductRepository_FindByDateRange_ReturnProductList() {
        // Arrange
        ProductType productType = ProductType.builder()
                .name("product")
                .build();

        Product product1 = Product.builder()
                .title("title1")
                .date(LocalDate.now().minusDays(5))
                .presence(0)
                .cost(1)
                .description("desc1")
                .productType(productType)
                .build();

        Product product2 = Product.builder()
                .title("title2")
                .date(LocalDate.now())
                .presence(0)
                .cost(1)
                .description("desc2")
                .productType(productType)
                .build();

        Product product3 = Product.builder()
                .title("product")
                .date(LocalDate.now().plusDays(10))
                .presence(0)
                .cost(1)
                .description("description")
                .productType(productType)
                .build();

        // Act
        productTypeRepository.save(productType);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        List<Product> products = productRepository.findByDateRange(LocalDate.now().minusDays(6), LocalDate.now().plusDays(1));

        // Assert
        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products.size()).isEqualTo(2);
    }
}