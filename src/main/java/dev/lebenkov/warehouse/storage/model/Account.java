package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotNull(message = "Username should not be empty")
    @Size(min = 4, max = 20, message = "Username should be 4 - 20 symbols size")
    @Column(name = "username")
    private String username;

    @NotNull(message = "Email should not be empty")
    @Email(message = "Email should be correct")
    @Column(name = "email")
    private String email;

    @NotNull(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @NotNull(message = "Firstname should not be empty")
    @Size(min = 2, max = 30, message = "Firstname should be 2 - 30 symbols size")
    @Column(name = "firstname")
    private String firstname;

    @NotNull(message = "Surname should not be empty")
    @Size(min = 3, max = 30, message = "Surname should be 3 - 30 symbols size")
    @Column(name = "surname")
    private String surname;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();
}
