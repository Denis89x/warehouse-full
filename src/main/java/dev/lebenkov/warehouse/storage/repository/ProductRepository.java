package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value =
            "SELECT p " +
                    "FROM Product p " +
                    "WHERE " +
                    "   p.title ILIKE %:keyword% OR " +
                    "   CAST(p.presence AS string ) ILIKE %:keyword% OR " +
                    "   CAST(p.cost AS string) ILIKE %:keyword% OR " +
                    "   p.description ILIKE %:keyword% "
    )
    List<Product> findByCustomQuery(String keyword);

    @Query(value =
            "SELECT p " +
                    "FROM Product p " +
                    "WHERE " +
                    "   p.date BETWEEN :startDate AND :endDate"
    )
    List<Product> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
