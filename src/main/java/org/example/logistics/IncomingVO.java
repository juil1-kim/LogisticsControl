package org.example.logistics;

import lombok.Data;

@Data
public class IncomingVO {
    private int incomingId;
    private int warehouseId; // Foreign Key
    private int productId; // Foreign Key
    private int supplierId; // Foreign Key
    private int quantity; // 입고 수량
    private String incomingDate; // DATETIME
}
