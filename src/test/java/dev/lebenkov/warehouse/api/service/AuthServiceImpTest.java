package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.security.JwtUtil;
import dev.lebenkov.warehouse.storage.dto.AuthRequest;
import dev.lebenkov.warehouse.storage.dto.AuthResponse;
import dev.lebenkov.warehouse.storage.dto.RegistrationRequest;
import dev.lebenkov.warehouse.storage.model.Account;
import dev.lebenkov.warehouse.storage.repository.AccountRepository;
import dev.lebenkov.warehouse.storage.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImpTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AccountDetailsService accountDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImp authService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldRegisterUserAndReturnTokens() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password");

        Account account = new Account();
        account.setUsername(registrationRequest.getUsername().toLowerCase());
        account.setEmail(registrationRequest.getEmail());
        account.setPassword("encodedPassword");

        when(accountRepository.findByUsername(registrationRequest.getUsername())).thenReturn(java.util.Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        when(accountDetailsService.loadUserByUsername(account.getUsername())).thenReturn(null);

        when(jwtUtil.generateToken(null)).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(null)).thenReturn("refreshToken");

        AuthResponse response = authService.register(registrationRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void shouldAuthenticateUserAndReturnTokens() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");

        Account account = new Account();
        account.setUsername(authRequest.getUsername());

        when(accountRepository.findByUsername(authRequest.getUsername())).thenReturn(java.util.Optional.of(account));
        when(jwtUtil.generateToken(eq(null))).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(eq(null))).thenReturn("refreshToken");

        AuthResponse response = authService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }
}