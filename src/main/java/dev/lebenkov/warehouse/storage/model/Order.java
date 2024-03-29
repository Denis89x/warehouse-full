package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @Column(name = "ord_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "ord_type")
    private String orderType;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderComposition> orderCompositions = new ArrayList<>();
}