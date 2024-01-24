package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.security.JwtUtil;
import dev.lebenkov.warehouse.storage.dto.AccountRequestLogin;
import dev.lebenkov.warehouse.storage.dto.AccountRequestRegistration;
import dev.lebenkov.warehouse.storage.model.Account;
import dev.lebenkov.warehouse.storage.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    JwtUtil jwtUtil;
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> register(AccountRequestRegistration accountRequestRegistration) {
        Account account = convertToAccount(accountRequestRegistration);

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole("ROLE_USER");

        accountRepository.save(account);

        return Map.of("jwt-token", jwtUtil.generateToken(account.getUsername()));
    }

    @Override
    public Map<String, String> login(AccountRequestLogin accountRequestLogin) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(accountRequestLogin.getUsername(),
                        accountRequestLogin.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        return Map.of("jwt-token", jwtUtil.generateToken(accountRequestLogin.getUsername()));
    }

    private Account convertToAccount(AccountRequestRegistration accountRequestRegistration) {
        return modelMapper.map(accountRequestRegistration, Account.class);
    }
}
