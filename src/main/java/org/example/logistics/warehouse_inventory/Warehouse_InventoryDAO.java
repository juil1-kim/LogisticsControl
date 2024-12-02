package org.example.logistics.warehouse_inventory;

import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Warehouse_InventoryDAO {
    private Connection con;

    public Warehouse_InventoryDAO() throws SQLException, ClassNotFoundException {
        this.con = DatabaseConnection.getConnection();
    }

    public ArrayList<Warehouse_InventoryVO> getAllInventoryDAO(int id) throws Exception {
        ArrayList<Warehouse_InventoryVO> InventoryList = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.price, wi.quantity, wi.last_updated " +
                "FROM Warehouse_Inventory wi JOIN Products p ON wi.product_id = p.product_id WHERE wi.warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Warehouse_InventoryVO wivo = new Warehouse_InventoryVO();
            wivo.setProductId(rs.getInt("product_id"));
            wivo.setProductName(rs.getString("name"));
            wivo.setProductPrice(rs.getDouble("price"));
            wivo.setQuantity(rs.getInt("quantity"));
            wivo.setLast_update(rs.getString("last_updated"));
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
        ps.executeUpdate();
    }
}