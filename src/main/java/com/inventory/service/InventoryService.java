package com.inventory.service;

import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }


    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public void updateInventory(Long id, Inventory updatedInventory) {

        Optional<Inventory> existingInventoryOpt = inventoryRepository.findById(id);

        if (existingInventoryOpt.isPresent()) {
            Inventory existingInventory = existingInventoryOpt.get();
            existingInventory.setName(updatedInventory.getName());
            existingInventory.setDescription(updatedInventory.getDescription());
            existingInventory.setPrice(updatedInventory.getPrice());
            existingInventory.setStockQuantity(updatedInventory.getStockQuantity());
            inventoryRepository.save(existingInventory);
        } else {
            throw new EntityNotFoundException("Inventory not found with ID: " + id);
        }
    }

    public void deleteInventoryById(Long id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Inventory with ID " + id + " does not exist.");
        }
    }

    public Page<Inventory> getPagedInventory(PageRequest pageRequest) {
        return inventoryRepository.findAll(pageRequest);
    }
}
