package com.inventory.controller;

import com.inventory.model.Inventory;
import com.inventory.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // GET method to retrieve all inventory items
    @GetMapping("/items")
    public ResponseEntity<List<Inventory>> getAllInventoryItems() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    // New GET method to retrieve paginated and sorted inventory items
    @GetMapping("/items/paged")
    public ResponseEntity<Page<Inventory>> getPagedAndSortedInventoryItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        PageRequest pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));
        Page<Inventory> inventoryPage = inventoryService.getPagedInventory(pageable);

        return new ResponseEntity<>(inventoryPage, HttpStatus.OK);
    }

    @GetMapping("/items/search")
    public ResponseEntity<List<Inventory>> searchInventory(@RequestParam("query") String query) {
        // Use the service layer to search inventory by name or description
        List<Inventory> searchResults = inventoryService.searchInventory(query);

        // Return a response with the search results
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }


    // POST request to create a new inventory item
    @PostMapping("/items")
    public ResponseEntity<Inventory> createInventoryItem(@RequestBody Inventory inventory) {
        // Use the service layer to save the new inventory item
        Inventory createdItem = inventoryService.createInventory(inventory);

        // Return a response with status 201 CREATED and the created item in the body
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Inventory> updateInventoryItem(
            @PathVariable Long id,
            @RequestBody Inventory updatedInventory
    ) {
        Inventory updatedItem = inventoryService.updateInventory(id, updatedInventory);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Inventory> deleteInventoryItem(@PathVariable Long id) {
        // Use the service layer to delete the inventory item by ID
        inventoryService.deleteInventoryById(id);

        // Return a 204 No Content response if deletion is successful
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
