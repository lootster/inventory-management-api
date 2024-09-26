package com.inventory.service;

import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    public Inventory addInventory(Inventory inventory) {
        return repository.save(inventory); // This should persist the inventory item
    }

}
