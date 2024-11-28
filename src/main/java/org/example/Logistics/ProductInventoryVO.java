package org.example.logistics;

import lombok.Data;

@Data
public class ProductInventoryVO {
    private int productId;
    private String productName;
    private int quantity;
    private String warehouseName;

    // AllArgsConstructor를 통해 생성자도 Lombok으로 처리 가능
    public ProductInventoryVO(int productId, String productName, int quantity, String warehouseName) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.warehouseName = warehouseName;
    }
}
