package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query(value =
            "SELECT s " +
                    "FROM Supplier s " +
                    "WHERE " +
                    "   s.title ILIKE %:keyword% OR " +
                    "   s.surname ILIKE %:keyword% OR " +
                    "   s.address ILIKE %:keyword% OR " +
                    "   s.phoneNumber ILIKE %:keyword% "
    )
    List<Supplier> findSimilarSupplier(String keyword);
}