package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @NotNull(message = "Name should not be empty")
    @Size(min = 3, max = 30, message = "Name should be 3 - 30 symbols size")
    @Column(name = "name")
    private String name;

/*    @ManyToMany(mappedBy = "stores")
    private List<Order> orders = new ArrayList<>();*/
}