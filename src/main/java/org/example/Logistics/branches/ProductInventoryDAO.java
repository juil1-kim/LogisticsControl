package org.example.logistics.branches;

import org.example.logistics.DatabaseConnection;
import org.example.logistics.ProductInventoryVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductInventoryDAO{

    public List<ProductInventoryVO> getProductInventory() {
        List<ProductInventoryVO> inventoryList = new ArrayList<>();
        String query = """
                SELECT 
                    p.product_id, p.name AS product_name, wi.quantity, w.name AS warehouse_name
                FROM 
                    Warehouse_Inventory wi
                JOIN 
                    Products p ON wi.product_id = p.product_id
                JOIN 
                    Warehouses w ON wi.warehouse_id = w.warehouse_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                String warehouseName = resultSet.getString("warehouse_name");

                inventoryList.add(new ProductInventoryVO(productId, productName, quantity, warehouseName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryList;
    }
}


