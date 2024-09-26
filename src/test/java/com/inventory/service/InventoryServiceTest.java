package com.inventory.service;

import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional  // Ensures tests are run in isolation with a rollback after each
class InventoryServiceTest {

    @Autowired
    private InventoryService service;  // Autowired InventoryService

    @Autowired
    private InventoryRepository repository;  // Repository to manage DB interaction

    @BeforeEach
    void setUp() {
        repository.deleteAll();  // Clean the database before each test
    }

    @Test
    void shouldLoadInventoryServiceBean() {
        // Assert
        assertNotNull(service, "InventoryService should not be null");
    }

    @Test
    void shouldLoadInventoryRepositoryBean() {
        // Assert
        assertNotNull(repository, "InventoryRepository should not be null");
    }

    @Test
    void shouldAddInventoryItem() {
        // Arrange
        Inventory inventory = new Inventory("Test Item", 10, BigDecimal.valueOf(99.99), "Test Description");

        // Act
        Inventory savedInventory = service.addInventory(inventory);

        // Assert
        assertNotNull(savedInventory);
        assertNotNull(savedInventory.getId()); // The ID should be automatically generated
        assertEquals("Test Item", savedInventory.getItemName());
        assertEquals(10, savedInventory.getStockQuantity());
        assertEquals(BigDecimal.valueOf(99.99), savedInventory.getPrice());

        // Verify via repository that the item is saved
        Inventory foundItem = repository.findById(savedInventory.getId()).orElse(null);
        assertNotNull(foundItem);
        assertEquals("Test Item", foundItem.getItemName());
    }

    @Test
    void shouldRetrieveAllInventories() {
        // Arrange: Create and save multiple inventory items
        Inventory inventory1 = new Inventory();
        inventory1.setItemName("Smartwatch");
        inventory1.setStockQuantity(100);
        inventory1.setPrice(BigDecimal.valueOf(250.00));
        inventory1.setDescription("Feature-rich smartwatch");

        Inventory inventory2 = new Inventory();
        inventory2.setItemName("Headphones");
        inventory2.setStockQuantity(200);
        inventory2.setPrice(BigDecimal.valueOf(100.00));
        inventory2.setDescription("Noise-cancelling headphones");

        repository.save(inventory1);
        repository.save(inventory2);

        // Act: Retrieve all inventories
        List<Inventory> allInventories = repository.findAll();

        // Assert: Verify that both inventories were retrieved
        assertEquals(2, allInventories.size());
    }

    @Test
    void shouldFindInventoryById() {
        // Arrange: Create and save an inventory item
        Inventory inventory = new Inventory();
        inventory.setItemName("Tablet");
        inventory.setStockQuantity(25);
        inventory.setPrice(BigDecimal.valueOf(400.00));
        inventory.setDescription("New tablet");

        Inventory savedInventory = repository.save(inventory);

        // Act: Retrieve the inventory by ID
        Optional<Inventory> foundInventory = repository.findById(savedInventory.getId());

        // Assert: Verify that the inventory was found
        assertTrue(foundInventory.isPresent(), "Inventory should be found by ID");
        assertEquals(savedInventory.getItemName(), foundInventory.get().getItemName());
    }

    @Test
    void shouldUpdateInventory() {
        // Arrange: Create and save an initial inventory item
        Inventory inventory = new Inventory();
        inventory.setItemName("Laptop");
        inventory.setStockQuantity(10);
        inventory.setPrice(BigDecimal.valueOf(1200.00));
        inventory.setDescription("High-end laptop");

        Inventory savedInventory = repository.save(inventory);

        // Act: Update the inventory
        savedInventory.setStockQuantity(20); // Change the stock quantity
        savedInventory.setPrice(BigDecimal.valueOf(1100.00)); // Update the price

        Inventory updatedInventory = repository.save(savedInventory);

        // Assert: Check if the update was successful
        assertNotNull(updatedInventory);
        assertEquals(20, updatedInventory.getStockQuantity());
        assertEquals(BigDecimal.valueOf(1100.00), updatedInventory.getPrice());
    }

    @Test
    void shouldDeleteInventory() {
        // Arrange: Create and save an inventory item
        Inventory inventory = new Inventory();
        inventory.setItemName("Smartphone");
        inventory.setStockQuantity(50);
        inventory.setPrice(BigDecimal.valueOf(700.00));
        inventory.setDescription("Latest model smartphone");

        Inventory savedInventory = repository.save(inventory);

        // Act: Delete the inventory
        repository.delete(savedInventory);

        // Assert: Verify that the inventory is no longer in the database
        Optional<Inventory> deletedInventory = repository.findById(savedInventory.getId());
        assertFalse(deletedInventory.isPresent(), "The inventory should have been deleted");
    }

    @Test
    void shouldReturnEmptyListWhenNoInventoriesPresent() {
        // Arrange: Ensure repository is empty
        repository.deleteAll();

        // Act: Retrieve all inventories
        List<Inventory> allInventories = repository.findAll();

        // Assert: Verify that the list is empty
        assertTrue(allInventories.isEmpty(), "Inventory list should be empty");
    }

    @Test
    void shouldReturnEmptyWhenInventoryNotFound() {
        // Act: Try to retrieve an inventory by a non-existing ID
        Optional<Inventory> inventory = repository.findById(999L);

        // Assert: Verify that the inventory is not found
        assertFalse(inventory.isPresent(), "Inventory with non-existing ID should not be found");
    }


}
