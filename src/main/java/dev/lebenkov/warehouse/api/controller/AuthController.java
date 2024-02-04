package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.AuthService;
import dev.lebenkov.warehouse.api.validation.AccountValidator;
import dev.lebenkov.warehouse.storage.dto.AccountRequestLogin;
import dev.lebenkov.warehouse.storage.dto.AccountRequestRegistration;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthController {

    AccountValidator accountValidator;
    AuthService authService;

    @PostMapping("/register")
    public Map<String, String> performRegistration(
            @RequestBody @Valid AccountRequestRegistration accountRequestRegistration,
            BindingResult bindingResult) {
        accountValidator.validate(accountRequestRegistration, bindingResult);

        if (bindingResult.hasErrors())
            return Map.of("Error", bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .toList()
                    .toString());

        return authService.register(accountRequestRegistration) ;
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(
            @RequestBody @Valid AccountRequestLogin accountRequestLogin) {
        return authService.login(accountRequestLogin);
    }
}
