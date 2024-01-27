package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.api.util.exception.StoreNotFoundException;
import dev.lebenkov.warehouse.storage.dto.StoreRequest;
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
public class StoreCRUDServiceImp implements StoreCRUDService {

    StoreRepository storeRepository;

    ModelMapper modelMapper;

    @Override
    public void saveStore(StoreRequest storeRequest) {
        storeRepository.save(convertToStore(storeRequest));
    }

    @Override
    public StoreResponse fetchStore(Long storeId) {
        return convertToStoreResponse(findStoreById(storeId));
    }

    @Override
    public List<StoreResponse> fetchAllStores() {
        return storeRepository.findAll().stream().map(this::convertToStoreResponse).toList();
    }

    @Override
    public void editStore(Long storeId, StoreRequest storeRequest) {
        Store store = findStoreById(storeId);

        store.setName(storeRequest.getName());
        storeRepository.save(store);
    }

    @Override
    public void deleteStore(Long storeId) {
        storeRepository.deleteById(storeId);
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("Store with " + storeId + " not found"));
    }

    private Store convertToStore(StoreRequest storeRequest) {
        return modelMapper.map(storeRequest, Store.class);
    }

    private StoreResponse convertToStoreResponse(Store store) {
        return modelMapper.map(store, StoreResponse.class);
    }
}