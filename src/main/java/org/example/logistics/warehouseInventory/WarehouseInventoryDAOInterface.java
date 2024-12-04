package org.example.logistics.warehouseInventory;

import java.util.ArrayList;

public interface WarehouseInventoryDAOInterface {
    ArrayList<IncomingVO> getAllInventoryOrders() throws Exception;

    ArrayList<WarehouseInventoryVO> getAllInventory(int id) throws Exception;

    //해당 창고에서 원하는 상품을 추가할 수 있다.
    //없는 상품은 새로 추가된다.
    void addInventory(int warehouseId, int productId, int supplierId, int quantity) throws Exception;
}
