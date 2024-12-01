package org.example.logistics.warehouse_inventory;

import lombok.Data;

@Data
public class Warehouse_InventoryVO {
    private int productId;
    private String productName;
    private Double productPrice;
    private int quantity;
}