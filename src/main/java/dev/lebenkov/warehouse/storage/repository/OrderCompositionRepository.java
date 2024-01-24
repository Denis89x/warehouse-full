package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.OrderComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCompositionRepository extends JpaRepository<OrderComposition, Long> {
}
