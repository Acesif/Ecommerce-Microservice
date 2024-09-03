package com.asif.inventoryservice.controller;

import com.asif.inventoryservice.model.Inventory;
import com.asif.inventoryservice.repository.InventoryRepository;
import com.asif.inventoryservice.service.InventoryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("skuCode") String skucode){
        return inventoryService.isInStock(skucode);
    }
}
