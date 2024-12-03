package org.example.logistics.administrators_warehouses;

import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Administrators_WarehousesDAO {
    private static Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public Administrators_WarehousesDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE : 관리자 - 창고 정보 추가
    public void addAdministrators_Warehouse(Administrators_WarehousesVO administrators_Warehouse) throws SQLException {
        String sql = "INSERT INTO Administrators_warehouses(admin_warehouse_id, admin_id, warehouse_id, assigned_at) VALUES(?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, administrators_Warehouse.getAdmin_id());
            stmt.setInt(2, administrators_Warehouse.getWarehouse_id());
            stmt.setTimestamp(3, administrators_Warehouse.getAssigned_at());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ ALL : 모든 관리자 - 창고 정보 가져오기
    public List<Administrators_WarehousesVO> getAllAdministrators_Warehouses() throws SQLException {
        List<Administrators_WarehousesVO> administrators_Warehouses = new ArrayList<>();
        String sql = "SELECT * FROM Administrators_warehouses";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Administrators_WarehousesVO administrators_Warehouse = new Administrators_WarehousesVO();
                administrators_Warehouse.setAdmin_warehouse_id(rs.getInt("admin_warehouse_id"));
                administrators_Warehouse.setAdmin_id(rs.getInt("admin_id"));
                administrators_Warehouse.setWarehouse_id(rs.getInt("warehouse_id"));
                administrators_Warehouse.setAssigned_at(rs.getTimestamp("Assinged_at"));
                administrators_Warehouses.add(administrators_Warehouse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return administrators_Warehouses;
    }

    // READ BY ID : 특정 관리자 - 창고 정보 가져오기
    public static Administrators_WarehousesVO getAdministrators_Warehouse(int admin_warehouse_id) throws SQLException {
        String sql = "SELECT * FROM Administrators_warehouses WHERE admin_warehouse_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, admin_warehouse_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Administrators_WarehousesVO administrators_Warehouse = new Administrators_WarehousesVO();
                administrators_Warehouse.setAdmin_id(rs.getInt("admin_warehouse_id"));
                administrators_Warehouse.setAdmin_id(rs.getInt("admin_id"));
                administrators_Warehouse.setWarehouse_id(rs.getInt("warehouse_id"));
                administrators_Warehouse.setAssigned_at(rs.getTimestamp("Assigned_at"));
                return administrators_Warehouse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE : 관리자 - 창고 정보 수정
    public void updateAdministrators_Warehouse(Administrators_WarehousesVO administrators_Warehouse) throws SQLException {
        String sql = "UPDATE Administrators_Warehouses WHERE admin_warehouse_id = ?, admin_id = ?, warehouse_id = ?, assigned_at = ? WHERE admin_warehouse_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, administrators_Warehouse.getAdmin_warehouse_id());
            stmt.setInt(2, administrators_Warehouse.getAdmin_id());
            stmt.setInt(3, administrators_Warehouse.getWarehouse_id());
            stmt.setTimestamp(4, administrators_Warehouse.getAssigned_at());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE : 관리자 - 창고 정보 삭제
    public void deleteAdministrators_Warehouse(int admin_warehouse_id) throws SQLException {
        String sql = "DELETE FROM Administrators_Warehouses WHERE admin_warehouse_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, admin_warehouse_id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
