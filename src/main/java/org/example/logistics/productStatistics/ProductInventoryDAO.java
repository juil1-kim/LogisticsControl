package org.example.logistics.productStatistics;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductInventoryDAO implements ProductInventoryDAOInterface {

    // 페이징된 데이터 조회 메서드
    @Override
    public List<ProductInventoryVO> getProductInventory(int offset, int limit) {
        List<ProductInventoryVO> inventoryList = new ArrayList<>();
        String query = """
        SELECT 
            p.product_id, 
            p.name AS product_name, 
            SUM(wi.quantity) AS total_quantity,
            GROUP_CONCAT(DISTINCT w.name SEPARATOR ', ') AS warehouse_names
        FROM 
            Warehouse_Inventory wi
        JOIN 
            Products p ON wi.product_id = p.product_id
        JOIN 
            Warehouses w ON wi.warehouse_id = w.warehouse_id
        GROUP BY 
            p.product_id, p.name
        LIMIT ? OFFSET ?;
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, limit);  // LIMIT 설정
            statement.setInt(2, offset); // OFFSET 설정

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    inventoryList.add(new ProductInventoryVO(
                            resultSet.getInt("product_id"),
                            resultSet.getString("product_name"),
                            resultSet.getInt("total_quantity"),
                            resultSet.getString("warehouse_names")
                    ));
                }
            }

            CRUDLogger.log("READ", "재고", "페이징된 제품 재고 조회 성공");

        } catch (SQLException e) {
            CRUDLogger.log("ERROR", "재고", "페이징된 제품 재고 조회 실패 - " + e.getMessage());
            throw new RuntimeException("제품 재고 조회 실패", e);
        }

        return inventoryList;
    }

    // 전체 데이터 조회 메서드
    @Override
    public List<ProductInventoryVO> getAllProductInventory() {
        List<ProductInventoryVO> inventoryList = new ArrayList<>();
        String query = """
        SELECT 
            p.product_id, 
            p.name AS product_name, 
            SUM(wi.quantity) AS total_quantity,
            GROUP_CONCAT(DISTINCT w.name SEPARATOR ', ') AS warehouse_names
        FROM 
            Warehouse_Inventory wi
        JOIN 
            Products p ON wi.product_id = p.product_id
        JOIN 
            Warehouses w ON wi.warehouse_id = w.warehouse_id
        GROUP BY 
            p.product_id, p.name;
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                inventoryList.add(new ProductInventoryVO(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("total_quantity"),
                        resultSet.getString("warehouse_names")
                ));
            }

            CRUDLogger.log("READ", "재고", "전체 제품 재고 조회 성공");

        } catch (SQLException e) {
            CRUDLogger.log("ERROR", "재고", "전체 제품 재고 조회 실패 - " + e.getMessage());
            throw new RuntimeException("전체 제품 재고 조회 실패", e);
        }

        return inventoryList;
    }
}

