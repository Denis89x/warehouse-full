package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
