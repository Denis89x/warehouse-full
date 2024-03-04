package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Store;
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
public class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void StoreRepository_FindSimilarStore_ReturnSimilarStores() {
        // Arrange
        Store store = Store.builder()
                .name("test1")
                .build();

        Store store1 = Store.builder()
                .name("test2")
                .build();

        Store store2 = Store.builder()
                .name("store")
                .build();

        // Act
        storeRepository.save(store);
        storeRepository.save(store1);
        storeRepository.save(store2);

        List<Store> stores = storeRepository.findSimilarStore("test");

        // Assert
        Assertions.assertThat(stores).isNotNull();
        Assertions.assertThat(stores.size()).isEqualTo(2);
    }
}