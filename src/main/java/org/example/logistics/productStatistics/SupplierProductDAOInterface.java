package org.example.logistics.productStatistics;

import java.util.List;

public interface SupplierProductDAOInterface {
    // 공급자별 공급 제품과 연락처 조회
    List<SupplierProductVO> getSupplierProducts();
}
