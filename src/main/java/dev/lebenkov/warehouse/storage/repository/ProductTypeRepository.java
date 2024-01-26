package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
