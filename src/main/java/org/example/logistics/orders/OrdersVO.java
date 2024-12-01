package org.example.logistics.orders;

import lombok.Data;

@Data
public class OrdersVO {
    private int orderId;
    private int warehouseId; // Foreign Key
    private int branchId; // Foreign Key
    private String orderDate; // DATETIME
    private String status; // ENUM('pending', 'completed', 'cancelled')
}
