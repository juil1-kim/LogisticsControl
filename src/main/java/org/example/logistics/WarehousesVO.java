package org.example.logistics;

import lombok.Data;

@Data
public class WarehousesVO {
    private int warehouseId;
    private String name;
    private String location;
    private int capacity; // 창고 용량
}
