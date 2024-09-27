package com.inventory.service;

import com.inventory.Service.InventoryService;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void clearInventory() {
        inventoryRepository.deleteAll();
    }

    @Test
    public void shouldSaveNewInventory() {
        // Arrange
        Inventory inventory = new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10);

        // Act
        Inventory savedInventory = inventoryService.createInventory(inventory);

        // Assert
        assertNotNull(savedInventory.getId());
        assertNotNull(inventoryRepository.findById(savedInventory.getId()));
    }

    @Test
    public void shouldRetrieveAllInventories() {
        // Arrange: Save inventories in the test database
        Inventory inventory1 = new Inventory("Item1", "Description1", new BigDecimal("100.00"), 10);
        Inventory inventory2 = new Inventory("Item2", "Description2", new BigDecimal("150.00"), 5);

        inventoryRepository.save(inventory1);  // Ensure the save step
        inventoryRepository.save(inventory2);

        // Act: Retrieve all inventories
        List<Inventory> inventoryList = inventoryService.getAllInventory();

        // Assert
        assertTrue(inventoryList.contains(inventory1), "Inventory list should contain inventory1");
        assertTrue(inventoryList.contains(inventory2), "Inventory list should contain inventory2");
    }


    @Test
    public void shouldRetrieveInventoryById() {
        // Arrange: Create and save an inventory item
        Inventory inventory = new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10);
        Inventory saved = inventoryService.createInventory(inventory);

        // Act: Retrieve the inventory item by ID
        Optional<Inventory> retrievedInventory = inventoryService.getInventoryById(saved.getId());

        // Assert: Verify the retrieved inventory is the same as the saved one
        assertTrue(retrievedInventory.isPresent(), "Inventory should be present");
        assertEquals(saved.getId(), retrievedInventory.get().getId());
        assertEquals(saved.getName(), retrievedInventory.get().getName());
        assertEquals(saved.getDescription(), retrievedInventory.get().getDescription());
        assertEquals(saved.getPrice(), retrievedInventory.get().getPrice());
        assertEquals(saved.getStockQuantity(), retrievedInventory.get().getStockQuantity());
    }

    @Test
    void shouldUpdateExistingInventory() {
        // Arrange
        Inventory inventory = new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10);
        inventoryService.createInventory(inventory);

        // Act
        inventory.setPrice(new BigDecimal("1000.00"));
        inventory.setStockQuantity(15);
        inventoryService.updateInventory(inventory.getId(), inventory);

        // Assert
        Optional<Inventory> updatedInventory = inventoryRepository.findById(inventory.getId());
        assertTrue(updatedInventory.isPresent(), "Inventory should be present after update");
        assertEquals(new BigDecimal("1000.00"), updatedInventory.get().getPrice());
        assertEquals(15, updatedInventory.get().getStockQuantity());
    }

    @Test
    void shouldDeleteInventoryById() {
        // Arrange
        Inventory inventory = new Inventory("Smartphone", "iPhone", new BigDecimal("999.99"), 10);
        Inventory savedInventory = inventoryService.createInventory(inventory);

        // Act
        inventoryService.deleteInventoryById(savedInventory.getId());

        // Assert
        Optional<Inventory> deletedInventory = inventoryRepository.findById(savedInventory.getId());
        assertFalse(deletedInventory.isPresent(), "Inventory should be deleted");
    }

    @Test
    public void shouldRetrievePagedAndSortedInventory() {
        // Arrange
        Inventory inventory1 = new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10);
        Inventory inventory2 = new Inventory("Mouse", "Logitech", new BigDecimal("50.00"), 100);
        Inventory inventory3 = new Inventory("Keyboard", "Corsair", new BigDecimal("150.00"), 50);

        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
        inventoryRepository.save(inventory3);

        // Act: Retrieve inventory sorted by name and limited to 2 per page
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("name").ascending());
        Page<Inventory> pagedInventory = inventoryService.getPagedInventory(pageRequest);

        // Assert
        assertEquals(2, pagedInventory.getContent().size());
        assertEquals("Keyboard", pagedInventory.getContent().get(0).getName());
        assertEquals("Laptop", pagedInventory.getContent().get(1).getName());
        assertTrue(pagedInventory.hasNext());
    }

    @Test
    public void shouldSearchInventoryByNameOrDescription() {
        // Arrange: Create and save inventory items
        Inventory inventory1 = new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10);
        Inventory inventory2 = new Inventory("Smartphone", "Apple iPhone", new BigDecimal("999.99"), 5);
        inventoryService.createInventory(inventory1);
        inventoryService.createInventory(inventory2);

        // Act: Search by name or description
        List<Inventory> searchResults = inventoryService.searchInventory("Laptop");

        // Assert: Verify the search results
        assertNotNull(searchResults);
        assertEquals(1, searchResults.size(), "Only one inventory item should match the search");
        assertEquals("Laptop", searchResults.get(0).getName());

        // Act: Search by description
        List<Inventory> searchResultsByDescription = inventoryService.searchInventory("Apple");

        // Assert: Verify the search results by description
        assertNotNull(searchResultsByDescription);
        assertEquals(1, searchResultsByDescription.size(), "Only one inventory item should match the search by description");
        assertEquals("Smartphone", searchResultsByDescription.get(0).getName());
    }

    @Test
    public void shouldFindInventoryBelowStockThreshold() {
        // Arrange
        Inventory item1 = new Inventory("Item1", "Low stock item", new BigDecimal("50.00"), 2);
        Inventory item2 = new Inventory("Item2", "Sufficient stock", new BigDecimal("75.00"), 20);
        inventoryRepository.save(item1);
        inventoryRepository.save(item2);

        // Act
        List<Inventory> itemsBelowThreshold = inventoryService.findInventoryBelowStockThreshold(5);

        // Assert
        assertTrue(itemsBelowThreshold.contains(item1), "Should include item1 as it's below threshold");
        assertFalse(itemsBelowThreshold.contains(item2), "Should not include item2 as it's above threshold");
    }

}
