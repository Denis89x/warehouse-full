package dev.lebenkov.warehouse.api.controller;

import dev.lebenkov.warehouse.api.service.AccountCredentialsService;
import dev.lebenkov.warehouse.storage.dto.ChangePasswordRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {

    AccountCredentialsService accountCredentialsService;

    public static final String CHANGE_PASSWORD = "/change-password";

    @PostMapping(CHANGE_PASSWORD)
    public ResponseEntity<String> changeAccountPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        accountCredentialsService.changePassword(changePasswordRequest);
        return new ResponseEntity<>("Password was successfully changed!", HttpStatus.OK);
    }
}
