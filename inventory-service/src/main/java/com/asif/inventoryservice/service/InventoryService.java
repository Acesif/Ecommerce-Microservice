package com.asif.inventoryservice.service;

import com.asif.inventoryservice.model.Inventory;
import com.asif.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skucode){
        Optional<Inventory> isInStock =inventoryRepository.findBySkuCode(skucode);
        return isInStock.isPresent();
    }
}
