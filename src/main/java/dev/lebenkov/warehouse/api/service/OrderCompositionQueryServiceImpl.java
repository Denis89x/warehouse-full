package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.model.OrderComposition;
import dev.lebenkov.warehouse.storage.repository.OrderCompositionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCompositionQueryServiceImpl implements OrderCompositionQueryService {

    OrderCompositionRepository orderCompositionRepository;

    @Override
    public List<OrderComposition> findOrderCompositionAndDateBetween(LocalDate startDate, LocalDate endDate) {
        return orderCompositionRepository.findOrderCompositionAndDateBetween(startDate, endDate);
    }
}
