package dev.lebenkov.warehouse.api.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Violation {
    private final String fieldName;
    private final String message;
}
