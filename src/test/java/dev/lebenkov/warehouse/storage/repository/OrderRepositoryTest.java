package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Order;
import dev.lebenkov.warehouse.storage.model.Store;
import dev.lebenkov.warehouse.storage.model.Supplier;
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
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void OrderRepository_FindSimilarOrder_ReturnOrderList() {
        // Arrange
        Supplier supplier = Supplier.builder()
                .title("supplier")
                .surname("surname")
                .phoneNumber("+375291111111")
                .address("address")
                .build();

        Store store = Store.builder()
                .name("store")
                .build();

        Order order1 = Order.builder()
                .orderType("Arrival")
                .date(LocalDate.now().minusDays(10))
                .amount(0)
                .store(store)
                .supplier(supplier)
                .build();

        Order order2 = Order.builder()
                .orderType("Disposal")
                .date(LocalDate.now())
                .amount(0)
                .store(store)
                .supplier(supplier)
                .build();

        // Act
        supplierRepository.save(supplier);
        storeRepository.save(store);

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> ordersByOrderType = orderRepository.findSimilarOrder("Arrival");
        List<Order> ordersByAmount = orderRepository.findSimilarOrder("0");
        List<Order> ordersByDate = orderRepository.findSimilarOrder(LocalDate.now().toString());

        // Assert
        Assertions.assertThat(ordersByOrderType).isNotNull();
        Assertions.assertThat(ordersByAmount).isNotNull();
        Assertions.assertThat(ordersByDate).isNotNull();

        Assertions.assertThat(ordersByOrderType.size()).isEqualTo(1);
        Assertions.assertThat(ordersByAmount.size()).isEqualTo(2);
        Assertions.assertThat(ordersByDate.size()).isEqualTo(1);
    }
}