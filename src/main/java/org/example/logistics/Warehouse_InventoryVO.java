package org.example.logistics;

import lombok.Data;

@Data
public class Warehouse_InventoryVO {
    private int inventoryId;
    private int warehouseId; // Foreign Key
    private int productId; // Foreign Key
    private int quantity; // 재고 수량
    private String lastUpdated; // DATETIME
}