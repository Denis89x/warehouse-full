package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Account;
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

    @Autowired
    private AccountRepository accountRepository;

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

    @Test
    public void OrderRepository_FindByDateRange_ReturnOrderList() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(5);

        Order order1 = Order.builder()
                .orderType("Arrival")
                .date(LocalDate.now().minusDays(10))
                .amount(0)
                .build();

        Order order2 = Order.builder()
                .orderType("Disposal")
                .date(LocalDate.now())
                .amount(0)
                .build();

        // Act
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findByDateRange(startDate, endDate);

        // Assert
        Assertions.assertThat(orders).isNotNull();
        Assertions.assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    public void OrderRepository_FindOrdersByAccountUsername_ReturnOrderList() {
        // Arrange
        Account account1 = Account.builder()
                .username("username1")
                .password("password")
                .email("email@mail.ru")
                .build();

        Account account2 = Account.builder()
                .username("username2")
                .password("password")
                .email("email@mail.ru")
                .build();

        Order order1 = Order.builder()
                .orderType("Arrival")
                .date(LocalDate.now().minusDays(10))
                .amount(0)
                .account(account1)
                .build();

        Order order2 = Order.builder()
                .orderType("Disposal")
                .date(LocalDate.now())
                .amount(0)
                .account(account2)
                .build();

        // Act
        accountRepository.save(account1);
        accountRepository.save(account2);

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findOrdersByAccount_Username(account1.getUsername());

        // Assert
        Assertions.assertThat(orders).isNotNull();
        Assertions.assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    public void OrderRepository_FindOrdersBySupplierIdAndOrderTypeAndDateBetween_ReturnOrderList() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(5);

        Supplier supplier1 = Supplier.builder()
                .title("supplier")
                .surname("surname")
                .phoneNumber("+375291111111")
                .address("address")
                .build();

        Supplier supplier2 = Supplier.builder()
                .title("supplier")
                .surname("surname")
                .phoneNumber("+375291111111")
                .address("address")
                .build();

        Order order1 = Order.builder()
                .orderType("Arrival")
                .date(LocalDate.now())
                .amount(0)
                .supplier(supplier1)
                .build();

        Order order2 = Order.builder()
                .orderType("Disposal")
                .date(LocalDate.now())
                .amount(0)
                .supplier(supplier2)
                .build();

        Order order3 = Order.builder()
                .orderType("Disposal")
                .date(LocalDate.now())
                .amount(0)
                .supplier(supplier1)
                .build();

        // Act
        supplierRepository.save(supplier1);
        supplierRepository.save(supplier2);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        List<Order> orders = orderRepository.findOrdersBySupplierIdAndOrderTypeAndDateBetween(
                supplier1.getSupplierId(), "Arrival", startDate, endDate);

        // Assert
        Assertions.assertThat(orders).isNotNull();
        Assertions.assertThat(orders.size()).isEqualTo(1);
    }
}