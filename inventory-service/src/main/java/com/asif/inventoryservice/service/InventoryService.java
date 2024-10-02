package com.asif.inventoryservice.service;

import com.asif.inventoryservice.dto.InventoryResponse;
import com.asif.inventoryservice.model.Inventory;
import com.asif.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skucode){
        return inventoryRepository.findAllBySkuCode(skucode)
                .stream().map(
                        inventory -> InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .availableQuantity(inventory.getQuantity())
                                .build()
                )
                .toList();
    }
}
