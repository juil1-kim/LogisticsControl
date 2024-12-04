package org.example.logistics.warehouseStatistics.warehouseStatistics;

import java.util.List;

public interface WarehouseStatisticsDAOInterface {
    // 창고 이름 목록 가져오기
    List<String> getWarehouseNames();

    // 특정 창고의 데이터 가져오기
    List<WarehouseStatisticsVO> getWarehouseDataByName(String warehouseName);
}
