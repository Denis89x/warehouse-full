package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value =
            "SELECT o " +
                    "FROM Order o " +
                    "WHERE " +
                    "   o.orderType ILIKE %:keyword% OR " +
                    "   CAST(o.date AS string) ILIKE %:keyword% OR " +
                    "   CAST(o.amount AS string) ILIKE %:keyword% OR " +
                    "   o.store.name ILIKE %:keyword% OR " +
                    "   o.supplier.title ILIKE %:keyword%"
    )
    List<Order> findSimilarOrder(String keyword);
}