package com.asif.inventoryservice;

import com.asif.inventoryservice.model.Inventory;
import com.asif.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    private void loadData(InventoryRepository inventoryRepository){
        Inventory item1 = Inventory.builder()
                .skuCode("amoogus")
                .quantity(100)
                .build();
        inventoryRepository.save(item1);
        Inventory item2 = Inventory.builder()
                .skuCode("gyaat")
                .quantity(109)
                .build();
        inventoryRepository.save(item2);
    }

}
