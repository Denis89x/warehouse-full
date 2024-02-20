package dev.lebenkov.warehouse.api.service;


import dev.lebenkov.warehouse.storage.dto.AuthResponse;
import dev.lebenkov.warehouse.storage.dto.AuthRequest;
import dev.lebenkov.warehouse.storage.dto.RegistrationRequest;
import dev.lebenkov.warehouse.storage.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface AuthService {
    AuthResponse register(RegistrationRequest registrationRequest);

    AuthResponse authenticate(AuthRequest authRequest);

    void saveUserToken(Account account, String jwtToken);

    void revokeAllUserTokens(Account account);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}