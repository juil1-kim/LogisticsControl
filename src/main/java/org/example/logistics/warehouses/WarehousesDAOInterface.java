package org.example.logistics.warehouses;

import java.util.List;

public interface WarehousesDAOInterface {
    //Create: 지점 생성
    void addWarehouses(String warehouse_name, String warehouse_loc, int warehouse_cap) throws Exception;

    //Read: 지점 목록
    List<WarehousesVO> getAllWarehouses() throws Exception;

    //Update: 지점 수정
    void updateWarehouses(String name, int id) throws Exception;

    //Delete: 지점 삭제
    void deleteWarehouses(int id) throws Exception;
}
