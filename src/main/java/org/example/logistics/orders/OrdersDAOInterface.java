package org.example.logistics.orders;

import java.util.List;

public interface OrdersDAOInterface {
    List<OrdersVO> getAllOrder() throws Exception;

    void addOrder(int warehouseId, int branchId, int productId, int quantity) throws Exception;

    void processOrder(int requestId);
}
