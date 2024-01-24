package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
