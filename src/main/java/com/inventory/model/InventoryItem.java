package com.inventory.model;

import java.math.BigDecimal;

public class InventoryItem {
    private String itemName;
    private int stockQuantity;
    private BigDecimal price;  // Adding price field
    private String description;

    // Constructor
    public InventoryItem(String itemName, int stockQuantity, BigDecimal price, String description) {
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.description = description;
    }

    // Getters
    public String getItemName() {
        return itemName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
