package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_composition")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_composition_id")
    private Long orderCompositionId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ord_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}

