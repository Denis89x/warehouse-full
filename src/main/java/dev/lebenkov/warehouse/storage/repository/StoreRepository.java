package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.name LIKE %:keyword%")
    List<Store> findSimilarStore(String keyword);
}