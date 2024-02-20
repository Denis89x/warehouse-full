package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.OrderComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderCompositionRepository extends JpaRepository<OrderComposition, Long> {

    @Query(value =
            "SELECT o " +
                    "FROM OrderComposition o " +
                    "WHERE o.order.date BETWEEN :startDate AND :endDate"
    )
    List<OrderComposition> findOrderCompositionAndDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
