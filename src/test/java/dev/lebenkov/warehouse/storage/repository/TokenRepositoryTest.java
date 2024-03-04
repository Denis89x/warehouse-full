package dev.lebenkov.warehouse.storage.repository;

import dev.lebenkov.warehouse.storage.model.Account;
import dev.lebenkov.warehouse.storage.model.Token;
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
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void TokenRepository_FindAllValidTokenByUser_ReturnTokenList() {
        // Arrange
        Account account = Account.builder()
                .username("username")
                .password("password")
                .email("email@mail.ru")
                .role("ROLE_USER")
                .build();

        Token token1 = Token.builder()
                .token("test1")
                .revoked(false)
                .expired(false)
                .account(account)
                .build();

        Token token2 = Token.builder()
                .token("test2")
                .revoked(false)
                .expired(false)
                .account(account)
                .build();

        Token token3 = Token.builder()
                .token("test3")
                .revoked(true)
                .expired(true)
                .account(account)
                .build();

        // Act
        accountRepository.save(account);

        tokenRepository.save(token1);
        tokenRepository.save(token2);
        tokenRepository.save(token3);

        List<Token> tokens = tokenRepository.findAllValidTokenByUser(account.getAccountId());

        // Assert
        Assertions.assertThat(tokens).isNotNull();
        Assertions.assertThat(tokens.size()).isEqualTo(2);
    }

    @Test
    public void TokenRepository_FindByToken_ReturnToken() {
        // Arrange
        Token token = Token.builder()
                .token("test token")
                .revoked(false)
                .expired(false)
                .build();

        // Act
        tokenRepository.save(token);

        Token tokenFind = tokenRepository.findByToken(token.getToken()).get();

        // Assert
        Assertions.assertThat(tokenFind).isNotNull();
        Assertions.assertThat(tokenFind.getToken()).isEqualTo(token.getToken());
    }
}