package org.example.logistics.productStatistics;

import java.util.List;

public interface ProductInventoryDAOInterface {
    // 페이징된 데이터 조회 메서드
    List<ProductInventoryVO> getProductInventory(int offset, int limit);

    // 전체 데이터 조회 메서드
    List<ProductInventoryVO> getAllProductInventory();
}
