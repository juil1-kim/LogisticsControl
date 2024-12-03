package org.example.logistics.administratorsWarehouses;

import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministratorsWarehousesDAO {
    private static Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    // 생성자 : DatabaseConnection 클래스에서 데이터베이스 연결 객체(Connection)를 가져옴.
    // 이 연결 객체를 통해 데이터베이스 작업(쿼리 실행)을 수행함.
    public AdministratorsWarehousesDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE : 새 관리자 - 창고 정보를 데이터베이스에 추가하는 메서드
    public void addAdministrators_Warehouse(AdministratorsWarehousesVO administrators_Warehouse) throws SQLException {
        String sql = "INSERT INTO Administrators_warehouses(admin_warehouse_id, admin_id, warehouse_id, assigned_at) VALUES(?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            // SQL 쿼리의 ? 자리에 값 채우기
            stmt.setInt(1, administrators_Warehouse.getAdmin_warehouse_id()); // 관리자-창고 ID
            stmt.setInt(2, administrators_Warehouse.getAdmin_id()); // 관리자 ID
            stmt.setInt(3, administrators_Warehouse.getWarehouse_id()); // 창고 ID
            stmt.setTimestamp(4, administrators_Warehouse.getAssigned_at()); // 할당 시간
            // 쿼리 실행(데이터 삽입)
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생시 예외 출력
        }
    }

    // READ ALL : 모든 관리자 - 창고 정보를 데이터베이스에서 가져오는 메서드
    public List<AdministratorsWarehousesVO> getAllAdministrators_Warehouses() throws SQLException {
        List<AdministratorsWarehousesVO> administrators_Warehouses = new ArrayList<>();
        String sql = "SELECT * FROM Administrators_warehouses";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery(); // SELECT 쿼리 실행
            // 결과 집합(ResultSet)에서 데이터를 읽어 VO 객체에 저장
            while(rs.next()) {
                AdministratorsWarehousesVO administrators_Warehouse = new AdministratorsWarehousesVO();
                administrators_Warehouse.setAdmin_warehouse_id(rs.getInt("admin_warehouse_id")); // 관리자-창고 ID
                administrators_Warehouse.setAdmin_id(rs.getInt("admin_id")); // 관리자 ID
                administrators_Warehouse.setWarehouse_id(rs.getInt("warehouse_id")); // 창고 ID
                administrators_Warehouse.setAssigned_at(rs.getTimestamp("Assinged_at")); // 할당 시간
                administrators_Warehouses.add(administrators_Warehouse); // 리스트에 추가
            }
        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생시 예외 출력
        }
        return administrators_Warehouses; // 리스트 반환
    }

    // READ BY ID : 특정 관리자 - 창고 ID로 정보를 가져오는 메서드
    public static AdministratorsWarehousesVO getAdministrators_Warehouse(int admin_warehouse_id) throws SQLException {
        String sql = "SELECT * FROM Administrators_warehouses WHERE admin_warehouse_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, admin_warehouse_id); // 특정 ID 설정
            ResultSet rs = stmt.executeQuery(); // SELECT 쿼리 실행
            if(rs.next()) {
                AdministratorsWarehousesVO administrators_Warehouse = new AdministratorsWarehousesVO();
                administrators_Warehouse.setAdmin_id(rs.getInt("admin_warehouse_id"));
                administrators_Warehouse.setAdmin_id(rs.getInt("admin_id"));
                administrators_Warehouse.setWarehouse_id(rs.getInt("warehouse_id"));
                administrators_Warehouse.setAssigned_at(rs.getTimestamp("Assigned_at"));
                return administrators_Warehouse; // 결과 반환
            }
        } catch (Exception e) {
            e.printStackTrace(); // 오류 발생시 예외 출력
        }
        return null; // 해당 ID가 없으면 null 반환
    }

    // UPDATE : 기존 관리자 - 창고 정보를 수정하는 메서드
    public void updateAdministrators_Warehouse(AdministratorsWarehousesVO administrators_Warehouse) throws SQLException {
        String sql = "UPDATE Administrators_Warehouses WHERE admin_warehouse_id = ?, admin_id = ?, warehouse_id = ?, assigned_at = ? WHERE admin_warehouse_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, administrators_Warehouse.getAdmin_id()); // 관리자 ID 수정
            stmt.setInt(2, administrators_Warehouse.getWarehouse_id()); // 창고 ID 수정
            stmt.setTimestamp(3, administrators_Warehouse.getAssigned_at()); // 할당 시간 수정
            stmt.setInt(4, administrators_Warehouse.getAdmin_warehouse_id()); // 대상 관리자-창고 ID
            stmt.executeUpdate(); // UPDATE 쿼리 실행
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE : 특정 관리자 - 창고 ID의 데이트를 삭제하는 메서드
    public void deleteAdministrators_Warehouse(int admin_warehouse_id) throws SQLException {
        String sql = "DELETE FROM Administrators_Warehouses WHERE admin_warehouse_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, admin_warehouse_id); // 삭제할 관리자 - 창고 ID 설정
            stmt.executeUpdate(); // DELETE 쿼리 실행
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
