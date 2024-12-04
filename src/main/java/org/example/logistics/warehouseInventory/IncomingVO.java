package org.example.logistics.warehouseInventory;

import lombok.Data;

@Data
public class IncomingVO {
    private int incomingId;
    private int warehouseId;
    private int productId;
    private int supplierId;
    private int quantity;
    private String incomingDate;
}