package org.example.logistics.warehouseInventory;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WarehouseInventoryDAO {
    private Connection con;

    public WarehouseInventoryDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
    }

    public ArrayList<WarehouseInventoryVO> getAllInventoryDAO(int id) throws Exception {
        ArrayList<WarehouseInventoryVO> InventoryList = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.price, wi.quantity, wi.last_updated " +
                "FROM Warehouse_Inventory wi JOIN Products p ON wi.product_id = p.product_id WHERE wi.warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            WarehouseInventoryVO wivo = new WarehouseInventoryVO();
            wivo.setProductId(rs.getInt("product_id"));
            wivo.setProductName(rs.getString("name"));
            wivo.setProductPrice(rs.getDouble("price"));
            wivo.setQuantity(rs.getInt("quantity"));
            wivo.setLast_update(rs.getString("last_updated"));
            CRUDLogger.log("READ", "창고", "창고별 재고: " + rs.getString("name"));
            InventoryList.add(wivo);
        }
        return InventoryList;
    }

    //해당 창고에서 원하는 상품을 추가할 수 있다.
    //없는 상품은 새로 추가된다.
    public void addInventoryDAO(int id, int productId, int quantity) throws Exception {
        String query = "INSERT INTO Warehouse_Inventory (warehouse_id, product_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity), last_updated = CURRENT_TIMESTAMP;";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, productId);
        ps.setInt(3, quantity);
        CRUDLogger.log("UPDATE", "창고", "창고ID: " + id + "상품ID: " + productId + "재고 추가: " + quantity);
        ps.executeUpdate();
    }
}