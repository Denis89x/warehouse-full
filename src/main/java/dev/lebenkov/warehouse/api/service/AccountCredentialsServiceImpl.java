package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.InvalidValidationException;
import dev.lebenkov.warehouse.storage.dto.ChangePasswordRequest;
import dev.lebenkov.warehouse.storage.model.Account;
import dev.lebenkov.warehouse.storage.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountCredentialsServiceImpl implements AccountCredentialsService {

    AccountRepository accountRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), getAccountFromSecurityHolderContext().getPassword())) {
            Account account = getAccountFromSecurityHolderContext();

            account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

            accountRepository.save(account);
        } else {
            throw new InvalidValidationException("Current password incorrect!");
        }
    }

    private Account getAccountFromSecurityHolderContext() {
        return accountRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Account not founded!"));
    }
}
