package com.inventory.service;

import com.inventory.model.InventoryItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

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
        InventoryItem item = new InventoryItem("Laptop", 10, BigDecimal.valueOf(1500.00), "A high-end laptop");

        // Act
        InventoryItem addedItem = service.addItem(item);

        // Assert
        assertNotNull(addedItem, "Added item should not be null");
        assertEquals("Laptop", addedItem.getItemName());
        assertEquals(10, addedItem.getStockQuantity());
        assertEquals(BigDecimal.valueOf(1500.00), addedItem.getPrice());  // Check price
        assertEquals("A high-end laptop", addedItem.getDescription());
    }
}
