package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Order;
import dev.lebenkov.warehouse.storage.model.OrderComposition;
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
class OrderCompositionRepositoryTest {

    @Autowired
    private OrderCompositionRepository orderCompositionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void OrderCompositionRepository_FindOrderCompositionAndDateBetween_ReturnOrderCompositionList() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(5);

        Order order1 = Order.builder()
                .date(LocalDate.now())
                .build();

        Order order2 = Order.builder()
                .date(LocalDate.now().minusDays(10))
                .build();

        OrderComposition orderComposition1 = OrderComposition.builder()
                .order(order1)
                .quantity(100)
                .build();

        OrderComposition orderComposition2 = OrderComposition.builder()
                .order(order1)
                .quantity(100)
                .build();

        OrderComposition orderComposition3 = OrderComposition.builder()
                .order(order2)
                .quantity(100)
                .build();

        OrderComposition orderComposition4 = OrderComposition.builder()
                .order(order2)
                .quantity(100)
                .build();

        // Act
        orderRepository.save(order1);
        orderRepository.save(order2);

        orderCompositionRepository.save(orderComposition1);
        orderCompositionRepository.save(orderComposition2);
        orderCompositionRepository.save(orderComposition3);
        orderCompositionRepository.save(orderComposition4);

        List<OrderComposition> orderCompositions = orderCompositionRepository.findOrderCompositionAndDateBetween(startDate, endDate);

        // Assert
        Assertions.assertThat(orderCompositions).isNotNull();
        Assertions.assertThat(orderCompositions.size()).isEqualTo(2);
    }
}