package dev.lebenkov.warehouse.api.service;


import dev.lebenkov.warehouse.storage.dto.AccountRequestLogin;
import dev.lebenkov.warehouse.storage.dto.AccountRequestRegistration;

import java.util.Map;

public interface AuthService {
    Map<String, String> register(AccountRequestRegistration accountRequestRegistration);
    Map<String, String> login(AccountRequestLogin accountRequestLogin);
}