package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductType {

    @Id
    @Column(name = "product_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productTypeId;

    @NotNull(message = "Name should not be empty")
    @Size(min = 3, max = 50, message = "Name should be 3 - 50 symbols size")
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "productType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();
}
