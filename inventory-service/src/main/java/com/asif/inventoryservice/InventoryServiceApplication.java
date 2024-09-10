package com.asif.inventoryservice;

import com.asif.inventoryservice.model.Inventory;
import com.asif.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory item1 = Inventory.builder()
                    .skuCode("amoogus")
                    .quantity(100)
                    .build();
            Inventory item2 = Inventory.builder()
                    .skuCode("gyaat")
                    .quantity(109)
                    .build();
            inventoryRepository.save(item1);
            inventoryRepository.save(item2);
        };
    }

}
