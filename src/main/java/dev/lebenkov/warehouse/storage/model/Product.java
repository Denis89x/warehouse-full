package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull(message = "Title should not be empty")
    @Size(min = 3, max = 50, message = "Title should be 3 - 50 symbols size")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Date should not be empty")
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "presence")
    private Integer presence;

    @NotNull(message = "Cost should not be empty")
    @Max(value = 1000, message = "Cost cannot be more then 1000")
    @Column(name = "cost")
    private Integer cost;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;

/*    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();*/

}