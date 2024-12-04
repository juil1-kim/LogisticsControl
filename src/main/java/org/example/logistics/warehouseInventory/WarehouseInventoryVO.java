package org.example.logistics.warehouseInventory;

import lombok.Data;

@Data
public class WarehouseInventoryVO {
    private int productId;
    private String productName;
    private Double productPrice;
    private int quantity;
    private String lastUpdate;
}