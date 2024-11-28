package org.example.logistics;

import lombok.Data;

@Data
public class SuppliersVO {
    private int supplierId;
    private String name;
    private String contact; // 전화번호
    private String location;
}

