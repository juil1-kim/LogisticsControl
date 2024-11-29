package org.example.logistics.productStatistics;

import lombok.Data;
import java.util.List;

@Data
public class SupplierProductVO {
    private String supplierName; // 공급자 이름
    private String contact;      // 공급자 연락처
    private List<String> products; // 공급 제품 목록
}

