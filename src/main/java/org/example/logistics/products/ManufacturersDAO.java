package org.example.logistics.products;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManufacturersDAO implements ManufacturersDAOInterface {

    // 제조사 목록 조회 (READ)
    @Override
    public List<ManufacturersVO> getAllManufacturers() {
        List<ManufacturersVO> manufacturers = new ArrayList<>();
        String query = "SELECT manufacturer_id, name, location, contact FROM Manufacturers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ManufacturersVO manufacturer = new ManufacturersVO();
                manufacturer.setManufacturerId(rs.getInt("manufacturer_id"));
                manufacturer.setName(rs.getString("name"));
                manufacturer.setLocation(rs.getString("location"));
                manufacturer.setContact(rs.getString("contact"));

                manufacturers.add(manufacturer);
            }

            // 로그 기록
            CRUDLogger.log("READ", "제조사", "전체 제조사 목록 조회 성공");

        } catch (SQLException e) {
            // 실패 로그 기록
            CRUDLogger.log("ERROR", "제조사", "전체 제조사 목록 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return manufacturers;
    }

    // 제조사 추가 (CREATE)
    @Override
    public boolean addManufacturer(String name, String location, String contact) {
        String query = "INSERT INTO Manufacturers (name, location, contact) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, contact);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                // 로그 기록
                CRUDLogger.log("CREATE", "제조사", "제조사 추가 성공: " + name);
            } else {
                CRUDLogger.log("WARN", "제조사", "제조사 추가 실패: " + name);
            }

            return success;

        } catch (SQLException e) {
            // 실패 로그 기록
            CRUDLogger.log("ERROR", "제조사", "제조사 추가 실패: " + name + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 제조사 수정 (UPDATE)
    @Override
    public boolean updateManufacturer(int id, String name, String location, String contact) {
        String query = "UPDATE Manufacturers SET name = ?, location = ?, contact = ? WHERE manufacturer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, contact);
            stmt.setInt(4, id);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                // 로그 기록
                CRUDLogger.log("UPDATE", "제조사", "제조사 수정 성공: ID " + id);
            } else {
                CRUDLogger.log("WARN", "제조사", "수정된 제조사 없음: ID " + id);
            }

            return success;

        } catch (SQLException e) {
            // 실패 로그 기록
            CRUDLogger.log("ERROR", "제조사", "제조사 수정 실패: ID " + id + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 제조사 삭제 (DELETE) -- id 기준
    @Override
    public boolean deleteManufacturer(int id) {
        String query = "DELETE FROM Manufacturers WHERE manufacturer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                // 로그 기록
                CRUDLogger.log("DELETE", "제조사", "제조사 삭제 성공: ID " + id);
            } else {
                CRUDLogger.log("WARN", "제조사", "삭제된 제조사 없음: ID " + id);
            }

            return success;

        } catch (SQLException e) {
            // 실패 로그 기록
            CRUDLogger.log("ERROR", "제조사", "제조사 삭제 실패: ID " + id + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}


