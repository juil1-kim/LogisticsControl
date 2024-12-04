package org.example.logistics.warehouseInventory;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WarehouseInventoryDAO implements WarehouseInventoryDAOInterface {
    private Connection con;

    public WarehouseInventoryDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
    }

    @Override
    public ArrayList<IncomingVO> getAllInventoryOrders() throws Exception {
        ArrayList<IncomingVO> InventoryOrders = new ArrayList<>();
        String query = "SELECT incoming_id, warehouse_id, product_id, quantity, incoming_date FROM Incoming";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            IncomingVO iv = new IncomingVO();
            iv.setIncomingId(rs.getInt("incoming_id"));
            iv.setWarehouseId(rs.getInt("warehouse_id"));
            iv.setProductId(rs.getInt("product_id"));
            iv.setQuantity(rs.getInt("quantity"));
            iv.setIncomingDate(rs.getString("incoming_date"));
            CRUDLogger.log("READ", "창고주문", "창고 주문ID: " + rs.getInt("incoming_id"));
            InventoryOrders.add(iv);
        }
        return InventoryOrders;
    }

    @Override
    public ArrayList<WarehouseInventoryVO> getAllInventory(int id) throws Exception {
        ArrayList<WarehouseInventoryVO> InventoryList = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.price, wi.quantity, wi.last_updated " +
                "FROM Warehouse_Inventory wi JOIN Products p ON wi.product_id = p.product_id WHERE wi.warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();
        if(!(rs.next())) {
            System.out.println("해당 창고는 없습니다.");
        }

        while (rs.next()) {
            WarehouseInventoryVO wivo = new WarehouseInventoryVO();
            wivo.setProductId(rs.getInt("product_id"));
            wivo.setProductName(rs.getString("name"));
            wivo.setProductPrice(rs.getDouble("price"));
            wivo.setQuantity(rs.getInt("quantity"));
            wivo.setLastUpdate(rs.getString("last_updated"));
            CRUDLogger.log("READ", "창고", "창고별 재고: " + rs.getString("name"));
            InventoryList.add(wivo);
        }

        return InventoryList;
    }

    //해당 창고에서 원하는 상품을 추가할 수 있다.
    //없는 상품은 새로 추가된다.
    @Override
    public void addInventory(int warehouseId, int productId, int supplierId, int quantity) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement incoming = null;
        PreparedStatement checkCapacityStmt = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            // 창고의 현재 재고 용량 확인
            String checkCapacityQuery = "SELECT COALESCE(SUM(quantity), 0) AS total_quantity, capacity FROM Warehouse_Inventory " +
                    "JOIN Warehouses ON Warehouse_Inventory.warehouse_id = Warehouses.warehouse_id WHERE Warehouse_Inventory.warehouse_id = ? " +
                    "GROUP BY Warehouses.warehouse_id, capacity;";
            checkCapacityStmt = con.prepareStatement(checkCapacityQuery);
            checkCapacityStmt.setInt(1, warehouseId);
            rs = checkCapacityStmt.executeQuery();
            // 총 수량이 없을경우 NULL을 반환하지만 COALESCE를 이용해서 0으로 반환하게 된다.
            int currentQuantity = 0;
            int warehouseCapacity = 0;

            if (rs.next()) {
                currentQuantity = rs.getInt("total_quantity");
                warehouseCapacity = rs.getInt("capacity");
            }
            // 롤백처리
            if (currentQuantity + quantity > warehouseCapacity) {
                throw new Exception("입고 요청이 창고 용량을 초과합니다.");
            }

            String query = "INSERT INTO Warehouse_Inventory (warehouse_id, product_id, quantity) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity), last_updated = CURRENT_TIMESTAMP;";
            ps = con.prepareStatement(query);
            ps.setInt(1, warehouseId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
            CRUDLogger.log("UPDATE", "창고", "창고ID: " + warehouseId + " 상품ID: " + productId + " 재고 추가: " + quantity);

            String incomingQuery = "INSERT INTO Incoming (warehouse_id, product_id, supplier_id, quantity) VALUES (?, ?, ?, ?)";
            incoming = con.prepareStatement(incomingQuery);
            incoming.setInt(1, warehouseId);
            incoming.setInt(2, productId);
            incoming.setInt(3, supplierId);
            incoming.setInt(4, quantity);
            incoming.executeUpdate();
            CRUDLogger.log("CREATE", "Incoming", "상품ID: " + productId + " 재고: " + quantity);

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (checkCapacityStmt != null) checkCapacityStmt.close();
            if (ps != null) ps.close();
            if (incoming != null) incoming.close();
            if (con != null) con.close();
        }
    }
}