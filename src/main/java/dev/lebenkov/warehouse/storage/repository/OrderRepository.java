package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
