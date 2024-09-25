package com.inventory.service;

import com.inventory.model.Inventory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class InventoryServiceTest {

    @Test
    void serviceExists() {
        // Act
        InventoryService service = new InventoryService();

        // Assert
        assertNotNull(service, "InventoryService should not be null");
    }

    @Test
    void addItemToService() {
        // Arrange
        InventoryService service = new InventoryService();
        Inventory item = new Inventory("Laptop", 10, BigDecimal.valueOf(1500.00), "A high-end laptop");

        // Act
        Inventory addedItem = service.addItem(item);

        // Assert
        assertNotNull(addedItem, "Added item should not be null");
        assertEquals("Laptop", addedItem.getItemName());
        assertEquals(10, addedItem.getStockQuantity());
        assertEquals(BigDecimal.valueOf(1500.00), addedItem.getPrice());  // Check price
        assertEquals("A high-end laptop", addedItem.getDescription());
    }

    @Test
    void getAllItemsFromService() {
        // Arrange
        InventoryService service = new InventoryService();
        Inventory item1 = new Inventory("Laptop", 10, BigDecimal.valueOf(1500.00), "A high-end laptop");
        Inventory item2 = new Inventory("Phone", 25, BigDecimal.valueOf(800.00), "A flagship smartphone");

        // Act
        service.addItem(item1);
        service.addItem(item2);
        List<Inventory> allItems = service.getAllItems();

        // Assert
        assertEquals(2, allItems.size());
        assertEquals("Laptop", allItems.get(0).getItemName());
        assertEquals("Phone", allItems.get(1).getItemName());
    }

    @Test
    void getItemById() {
        // Arrange
        InventoryService service = new InventoryService();
        Inventory item = new Inventory("Laptop", 10, BigDecimal.valueOf(1500.00), "A high-end laptop");
        service.addItem(item);

        // Act
        Optional<Inventory> foundItem = service.getItemById(item.getId());

        // Assert
        assertTrue(foundItem.isPresent(), "Item should be found");
        assertEquals("Laptop", foundItem.get().getItemName());
    }

    @Test
    void updateItemInService() {
        // Arrange
        InventoryService service = new InventoryService();
        Inventory item = new Inventory("Laptop", 10, BigDecimal.valueOf(1500.00), "A high-end laptop");
        service.addItem(item);

        // Act
        Inventory updatedItem = new Inventory("Gaming Laptop", 5, BigDecimal.valueOf(2000.00), "A high-end gaming laptop");
        Inventory result = service.updateItem(item.getId(), updatedItem);

        // Assert
        assertEquals("Gaming Laptop", result.getItemName());
        assertEquals(5, result.getStockQuantity());
        assertEquals(BigDecimal.valueOf(2000.00), result.getPrice());
        assertEquals("A high-end gaming laptop", result.getDescription());
    }

}
