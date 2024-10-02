package com.asif.inventoryservice.repository;

import com.asif.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    @Query("SELECT i FROM Inventory i WHERE i.skuCode IN :skucodes")
    List<Inventory> findAllBySkuCode(@Param("skucodes") List<String> skucodes);
}
