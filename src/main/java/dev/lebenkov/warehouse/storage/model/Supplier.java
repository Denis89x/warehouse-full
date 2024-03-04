package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @Column(name = "supplier_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @NotBlank(message = "Title should not be empty")
    @Size(min = 5, max = 50, message = "Title should be 5 - 50 symbols size")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Surname should not be empty")
    @Size(min = 5, max = 50, message = "Surname should be 5 - 50 symbols size")
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = "Address should not be empty")
    @Size(min = 7, max = 50, message = "Address should be 7 - 50 symbols size")
    @Column(name = "address")
    private String address;

    @NotBlank(message = "Phone number should not be empty")
    @Size(min = 7, max = 20, message = "Phone number should be 7 - 20 symbols size")
    @Column(name = "phone_number")
    private String phoneNumber;

    @ToString.Exclude
    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();
}
