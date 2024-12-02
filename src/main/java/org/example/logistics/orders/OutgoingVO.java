package org.example.logistics.orders;

import lombok.Data;

@Data
public class OutgoingVO {
    private int outgoingId;
    private int warehouseId; // Foreign Key
    private int productId; // Foreign Key
    private int orderId; // Foreign Key
    private int quantity; // 출고 수량
    private String outgoingDate; // DATETIME
}

