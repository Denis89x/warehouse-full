package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.StoreResponse;
import dev.lebenkov.warehouse.storage.model.Store;
import dev.lebenkov.warehouse.storage.repository.StoreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreQueryServiceImpl implements StoreQueryService {

    StoreRepository storeRepository;

    ModelMapper modelMapper;

    @Override
    public List<StoreResponse> findSimilarStore(String field) {
        return storeRepository.findSimilarStore(field).stream().map(this::convertToStoreResponse).toList();
    }

    private StoreResponse convertToStoreResponse(Store store) {
        return modelMapper.map(store, StoreResponse.class);
    }
}