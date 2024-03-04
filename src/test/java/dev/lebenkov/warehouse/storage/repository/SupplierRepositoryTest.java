package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Supplier;
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
class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    public void SupplierRepository_FindSimilarSupplier_ReturnSimilarSuppliers() {
        // Arrange
        Supplier supplier1 = Supplier.builder()
                .title("supplier1")
                .surname("surname1")
                .phoneNumber("+375291111111")
                .address("address1")
                .build();

        Supplier supplier2 = Supplier.builder()
                .title("supplier2")
                .surname("surname2")
                .phoneNumber("+375291111111")
                .address("address2")
                .build();

        Supplier supplier3 = Supplier.builder()
                .title("testTest")
                .surname("testTest")
                .phoneNumber("+375291111111")
                .address("testTest")
                .build();

        // Act
        supplierRepository.save(supplier1);
        supplierRepository.save(supplier2);
        supplierRepository.save(supplier3);

        List<Supplier> suppliersBySurname = supplierRepository.findSimilarSupplier("sur");
        List<Supplier> suppliersByTitle = supplierRepository.findSimilarSupplier("supp");
        List<Supplier> suppliersByAddress = supplierRepository.findSimilarSupplier("addr");
        List<Supplier> suppliersByPhoneNumber = supplierRepository.findSimilarSupplier("+375291");

        // Assert
        Assertions.assertThat(suppliersBySurname).isNotNull();
        Assertions.assertThat(suppliersByTitle).isNotNull();
        Assertions.assertThat(suppliersByAddress).isNotNull();
        Assertions.assertThat(suppliersByPhoneNumber).isNotNull();

        Assertions.assertThat(suppliersBySurname.size()).isEqualTo(2);
        Assertions.assertThat(suppliersByTitle.size()).isEqualTo(2);
        Assertions.assertThat(suppliersByAddress.size()).isEqualTo(2);
        Assertions.assertThat(suppliersByPhoneNumber.size()).isEqualTo(3);
    }
}