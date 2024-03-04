package dev.lebenkov.warehouse.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@ToString
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotBlank(message = "Username should not be empty")
    @Size(min = 4, max = 20, message = "Username should be 4 - 20 symbols size")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be correct")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @ToString.Exclude
    @OneToMany(mappedBy = "account")
    private List<Token> tokens;

    @ToString.Exclude
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();
}
