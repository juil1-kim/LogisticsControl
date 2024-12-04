package org.example.logistics.warehouseStatistics;

import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseStatisticsDAO {

    // 창고 이름 목록 가져오기
    public List<String> getWarehouseNames() {
        List<String> warehouseNames = new ArrayList<>();
        String query = "SELECT DISTINCT name FROM Warehouses";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                warehouseNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouseNames;
    }

    // 특정 창고의 데이터 가져오기
    public List<WarehouseStatisticsVO> getWarehouseDataByName(String warehouseName) {
        List<WarehouseStatisticsVO> warehouseData = new ArrayList<>();
        String query = """
                SELECT w.warehouse_id, w.name AS warehouse_name, 
                       p.product_id, p.name AS product_name, i.quantity
                FROM Warehouse_Inventory i
                JOIN Warehouses w ON i.warehouse_id = w.warehouse_id
                JOIN Products p ON i.product_id = p.product_id
                WHERE w.name = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, warehouseName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WarehouseStatisticsVO vo = new WarehouseStatisticsVO();
                vo.setWarehouseId(rs.getInt("warehouse_id"));
                vo.setWarehouseName(rs.getString("warehouse_name"));
                vo.setProductId(rs.getInt("product_id"));
                vo.setProductName(rs.getString("product_name"));
                vo.setQuantity(rs.getInt("quantity"));
                warehouseData.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouseData;
    }
}


