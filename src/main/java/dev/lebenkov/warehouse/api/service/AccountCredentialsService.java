package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ChangePasswordRequest;

public interface AccountCredentialsService {
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
