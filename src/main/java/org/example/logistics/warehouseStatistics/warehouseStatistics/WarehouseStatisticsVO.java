package org.example.logistics.warehouseStatistics.warehouseStatistics;

import lombok.Data;

@Data
public class WarehouseStatisticsVO {
    private int warehouseId;       // 창고 ID
    private String warehouseName;  // 창고 이름
    private int productId;         // 제품 ID
    private String productName;    // 제품 이름
    private int quantity;          // 재고 수량
}

