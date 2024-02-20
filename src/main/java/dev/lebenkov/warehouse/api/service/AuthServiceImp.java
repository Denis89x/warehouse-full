package dev.lebenkov.warehouse.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lebenkov.warehouse.api.security.JwtUtil;
import dev.lebenkov.warehouse.api.util.exception.AccountAlreadyExistsException;
import dev.lebenkov.warehouse.api.util.exception.AccountNotFoundException;
import dev.lebenkov.warehouse.api.validation.Violation;
import dev.lebenkov.warehouse.storage.dto.AuthRequest;
import dev.lebenkov.warehouse.storage.dto.AuthResponse;
import dev.lebenkov.warehouse.storage.dto.RegistrationRequest;
import dev.lebenkov.warehouse.storage.enums.TokenType;
import dev.lebenkov.warehouse.storage.model.Account;
import dev.lebenkov.warehouse.storage.model.Token;
import dev.lebenkov.warehouse.storage.repository.AccountRepository;
import dev.lebenkov.warehouse.storage.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    AccountRepository accountRepository;
    TokenRepository tokenRepository;

    AccountDetailsService accountDetailsService;
    JwtUtil jwtUtil;

    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegistrationRequest registrationRequest) {
        accountRepository.findByUsername(registrationRequest.getUsername())
                .ifPresent(existingAccount -> {
                    throw new AccountAlreadyExistsException("Account with username " + registrationRequest.getUsername() + " already exists.");
                });

        Account account = Account.builder()
                .username(registrationRequest.getUsername().toLowerCase())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role("ROLE_USER")
                .build();

        Account savedAccount = accountRepository.save(account);

        UserDetails user = accountDetailsService.loadUserByUsername(account.getUsername());

        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        saveUserToken(savedAccount, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        Account account = accountRepository.findByUsername(authRequest.getUsername()).orElseThrow(() ->
                new AccountNotFoundException("Account with username " + authRequest.getUsername() + " not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails user = accountDetailsService.loadUserByUsername(authRequest.getUsername());

        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        revokeAllUserTokens(account);

        saveUserToken(account, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void saveUserToken(Account account, String jwtToken) {
        Token token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(Account account) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(account.getAccountId());

        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);

        username = jwtUtil.extractUsername(refreshToken);

        if (username != null) {
            UserDetails user = accountDetailsService.loadUserByUsername(username);
            Account account = accountRepository.findByUsername(username).orElseThrow();

            if (jwtUtil.isTokenValid(refreshToken, user)) {
                String accessToken = jwtUtil.generateToken(user);

                revokeAllUserTokens(account);

                saveUserToken(account, accessToken);

                AuthResponse authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
