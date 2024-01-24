package dev.lebenkov.warehouse.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String profilePicture;
}