package com.inventory.service;

import com.inventory.model.Inventory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryService {
    private List<Inventory> inventoryList = new ArrayList<>();

    public Inventory addItem(Inventory item) {
        inventoryList.add(item);
        return item;
    }

    public List<Inventory> getAllItems() {
        return inventoryList;
    }

    public Optional<Inventory> getItemById(Long id) {
        return inventoryList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public Inventory updateItem(Long id, Inventory updatedItem) {
        Optional<Inventory> existingItem = getItemById(id);
        if (existingItem.isPresent()) {
            Inventory item = existingItem.get();
            item.setItemName(updatedItem.getItemName());
            item.setStockQuantity(updatedItem.getStockQuantity());
            item.setPrice(updatedItem.getPrice());
            item.setDescription(updatedItem.getDescription());
            return item;
        } else {
            return null;  // Handle error case properly later
        }
    }
}
