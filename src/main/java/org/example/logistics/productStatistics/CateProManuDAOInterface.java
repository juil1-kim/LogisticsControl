package org.example.logistics.productStatistics;

import java.util.List;

public interface CateProManuDAOInterface {
    // 카테고리, 상품, 제조사 정보를 가져오는 메서드
    List<CategoryProductManufacturerVO> getCategoryProductManufacturers();
}
