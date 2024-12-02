package org.example.logistics.products;

import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManufacturersDAO {

    // 제조사 목록 조회 (READ)
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return manufacturers;
    }

    // 제조사 추가 (CREATE)
    public boolean addManufacturer(String name, String location, String contact) {
        String query = "INSERT INTO Manufacturers (name, location, contact) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, contact);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 제조사 수정 (UPDATE)
    public boolean updateManufacturer(int id, String name, String location, String contact) {
        String query = "UPDATE Manufacturers SET name = ?, location = ?, contact = ? WHERE manufacturer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setString(3, contact);
            stmt.setInt(4, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 제조사 삭제 (DELETE) -- id 기준
    public boolean deleteManufacturer(int id) {
        String query = "DELETE FROM Manufacturers WHERE manufacturer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

