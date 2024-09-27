package com.inventory.repository;

import com.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // This interface will inherit all the CRUD methods like save(), findById(), findAll(), etc.

    // Custom search query to search by name or description
    @Query("SELECT i FROM Inventory i WHERE i.name LIKE %:name% OR i.description LIKE %:description%")
    List<Inventory> searchInventory(String name, String description);


    @Query("SELECT i FROM Inventory i WHERE i.stockQuantity < :threshold")
    List<Inventory> findByStockQuantityLessThan(int threshold);
}