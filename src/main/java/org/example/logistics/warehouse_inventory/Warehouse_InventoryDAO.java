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
        String query = "SELECT p.product_id, p.name, p.price, wi.quantity " +
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
            InventoryList.add(wivo);
        }
        return InventoryList;
    }

    public void addInventoryDAO() throws Exception {
        String query = "SELECT p.product_id, p.name, p.price, wi.quantity " +
                "FROM Warehouse_Inventory wi JOIN Products p ON wi.product_id = p.product_id WHERE wi.warehouse_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
    }
}