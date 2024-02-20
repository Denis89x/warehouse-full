package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.model.OrderComposition;

import java.time.LocalDate;
import java.util.List;

public interface OrderCompositionQueryService {
    List<OrderComposition> findOrderCompositionAndDateBetween(LocalDate startDate, LocalDate endDate);
}
