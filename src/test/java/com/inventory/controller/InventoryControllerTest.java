package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryRepository inventoryRepository;  // Ensure this is properly injected

    @BeforeEach
    public void setUp() {
        // Clear the inventory table before each test
        inventoryRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllInventoryItems() throws Exception {
        inventoryRepository.save(new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10));
        inventoryRepository.save(new Inventory("Mouse", "Logitech", new BigDecimal("25.00"), 100));

        // Act: Perform GET request and expect a JSON response
        mockMvc.perform(get("/api/inventory/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'name':'Laptop'}, {'name':'Mouse'}]"));
    }

    @Test
    public void shouldCreateInventoryItem() throws Exception {
        Inventory newInventoryItem = new Inventory("Laptop", "Dell XPS", new BigDecimal("1200.00"), 10);

        // Convert the newInventoryItem object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(newInventoryItem);

        mockMvc.perform(post("/api/inventory/items")
                        .content(jsonContent)  // Pass the JSON content
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldUpdateInventoryItem() throws Exception {
        // Arrange: Create a new inventory item that will be updated
        Inventory existingItem = new Inventory("Keyboard", "Mechanical Keyboard", new BigDecimal("150.00"), 30);
        inventoryRepository.save(existingItem);

        // Prepare updated data
        existingItem.setPrice(new BigDecimal("100.00"));
        existingItem.setStockQuantity(40);

        // Convert the updated item object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String updatedItemJson = objectMapper.writeValueAsString(existingItem);

        // Act & Assert: Perform PUT request and expect a 200 OK status and updated fields
        mockMvc.perform(put("/api/inventory/items/{id}", existingItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedItemJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'price':100.00, 'stockQuantity':40}"));
    }

    @Test
    public void shouldDeleteInventoryItem() throws Exception {
        // Arrange: Create and save an inventory item
        Inventory itemToDelete = new Inventory("Monitor", "4K Monitor", new BigDecimal("400.00"), 20);
        itemToDelete = inventoryRepository.save(itemToDelete);

        // Act & Assert: Perform DELETE request and expect a 204 No Content status
        mockMvc.perform(delete("/api/inventory/items/{id}", itemToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify: Check if the item has been deleted
        boolean itemExists = inventoryRepository.findById(itemToDelete.getId()).isPresent();
        assertFalse(itemExists, "Inventory item should be deleted");
    }

    @Test
    public void shouldReturnPagedAndSortedInventoryItems() throws Exception {
        // Arrange: Create multiple inventory items
        inventoryRepository.save(new Inventory("Keyboard", "Mechanical Keyboard", new BigDecimal("150.00"), 30));
        inventoryRepository.save(new Inventory("Monitor", "Dell Monitor", new BigDecimal("300.00"), 15));
        inventoryRepository.save(new Inventory("Chair", "Gaming Chair", new BigDecimal("250.00"), 5));

        // Act & Assert: Perform GET request with pagination and sorting parameters
        mockMvc.perform(get("/api/inventory/items/paged")  // Updated endpoint to paged one
                        .param("page", "0")  // Requesting the first page
                        .param("size", "2")   // Two items per page
                        .param("sortBy", "name")  // Sorting by 'name' field
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Adjusted to match the Page structure with the `content` field
                .andExpect(jsonPath("$.content.length()").value(2))  // Ensure 2 items in 'content'
                .andExpect(jsonPath("$.content[0].name").value("Chair"))  // First item is Chair
                .andExpect(jsonPath("$.content[1].name").value("Keyboard"));  // Second item is Keyboard
    }

    @Test
    public void shouldSearchInventoryByNameOrDescription() throws Exception {
        // Arrange: Create and save inventory items
        inventoryRepository.save(new Inventory("Laptop", "Dell XPS Laptop", new BigDecimal("1200.00"), 10));
        inventoryRepository.save(new Inventory("Smartphone", "Apple iPhone", new BigDecimal("999.99"), 5));

        // Act & Assert: Perform GET request with search parameter 'Laptop'
        mockMvc.perform(get("/api/inventory/items/search")
                        .param("query", "Laptop")  // Searching by name
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))  // Ensure one item is returned
                .andExpect(jsonPath("$[0].name").value("Laptop"));  // First item is Laptop

        // Act & Assert: Perform GET request with search parameter 'Apple'
        mockMvc.perform(get("/api/inventory/items/search")
                        .param("query", "Apple")  // Searching by description
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))  // Ensure one item is returned
                .andExpect(jsonPath("$[0].name").value("Smartphone"));  // First item is Smartphone
    }

}
