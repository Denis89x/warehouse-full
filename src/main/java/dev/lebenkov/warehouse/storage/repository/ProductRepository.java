package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
