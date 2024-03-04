package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {"spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=true"})
public class AccountRepositoryTest {


    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void AccountRepository_SaveAll_ReturnSavedAccount() {
        // Arrange
        Account account = Account.builder()
                .username("test")
                .password("password")
                .email("test@mail.ru")
                .role("ROLE_USER")
                .build();

        // Act
        Account savedAccount = accountRepository.save(account);

        // Assert
        Assertions.assertThat(savedAccount).isNotNull();
        Assertions.assertThat(savedAccount.getAccountId()).isGreaterThan(0);
    }

    @Test
    public void AccountRepository_GetAll_ReturnMoreThenOneAccount() {
        // Arrange
        Account account = Account.builder()
                .username("test")
                .password("password")
                .email("test@mail.ru")
                .role("ROLE_USER")
                .build();

        Account account2 = Account.builder()
                .username("test")
                .password("password")
                .email("test@mail.ru")
                .role("ROLE_USER")
                .build();

        // Act
        accountRepository.save(account);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findAll();

        // Assert
        Assertions.assertThat(accounts).isNotNull();
        Assertions.assertThat(accounts.size()).isEqualTo(2);
    }

    @Test
    public void AccountRepository_FindById_ReturnAccount() {
        // Arrange
        Account account = Account.builder()
                .username("test")
                .password("password")
                .email("test@mail.ru")
                .role("ROLE_USER")
                .build();

        // Act
        accountRepository.save(account);

        Account account1 = accountRepository.findById(account.getAccountId()).get();

        // Assert
        Assertions.assertThat(account1).isNotNull();
    }

    @Test
    public void AccountRepository_FindByUsername_ReturnAccount() {
        // Arrange
        Account account = Account.builder()
                .username("test")
                .password("password")
                .email("test@mail.ru")
                .role("ROLE_USER")
                .build();

        // Act
        accountRepository.save(account);

        Account accountFind = accountRepository.findByUsername(account.getUsername()).get();

        // Assert
        Assertions.assertThat(accountFind).isNotNull();
    }
}