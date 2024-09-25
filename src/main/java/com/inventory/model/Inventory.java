package com.inventory.model;

import java.math.BigDecimal;

public class Inventory {

    private Long id;  // Add ID field
    private String itemName;
    private int stockQuantity;
    private BigDecimal price;
    private String description;

    private static Long idCounter = 0L;  // Simple ID generator

    // Constructor
    public Inventory(String itemName, int stockQuantity, BigDecimal price, String description) {
        this.id = ++idCounter;  // Increment ID
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.description = description;
    }

    // Getters
    public Long getId() {
        return id;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void setIdCounter(Long idCounter) {
        Inventory.idCounter = idCounter;
    }
}
