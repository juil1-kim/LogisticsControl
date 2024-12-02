package org.example.logistics.suppliers;

import org.example.logistics.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuppliersDAO {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public SuppliersDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE : 공급자 정보 추가
    public void addSupplier(SuppliersVO supplier) throws SQLException {
        String sql = "INSERT INTO Suppliers(name, contact, location) VALUES(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContact());
            stmt.setString(3, supplier.getLocation());
            stmt.executeUpdate();
        }
    }

    // READ ALL : 모든 공급자 정보 가져오기
    public List<SuppliersVO> getAllSuppliers(int supplierId) throws SQLException {
        List<SuppliersVO> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SuppliersVO supplier = new SuppliersVO();
                supplier.setSupplierId(rs.getInt("supplierId"));
                supplier.setName(rs.getString("name"));
                supplier.setContact(rs.getString("contact"));
                supplier.setLocation(rs.getString("location"));
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    // READ BY ID : 특정 공급자 정보 가져오기
    public SuppliersVO getSuppliersById(int supplierId) throws SQLException {
        //SuppliersVO supplier = new SuppliersVO(); --> 중복자 선언으로 인한 에러가 났었음.
        String sql = "SELECT * FROM Suppliers WHERE supplierId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                SuppliersVO supplier = new SuppliersVO();
                supplier.setSupplierId(rs.getInt("supplierId"));
                supplier.setName(rs.getString("name"));
                supplier.setContact(rs.getString("contact"));
                supplier.setLocation(rs.getString("location"));
                return supplier;
            }
        }
        return null;
    }

    // UPDATE : 공급자 정보 수정
    public void updateSupplier(SuppliersVO supplier) throws SQLException {
        String sql = "UPDATE Suppliers SET name = ?, contact = ?, location = ? WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContact());
            stmt.setString(3, supplier.getLocation());
            stmt.setInt(4, supplier.getSupplierId());
            stmt.executeUpdate();
        }
    }

    // DELETE : 공급자 정보 삭제
    public void deleteSupplier(int supplierId) throws SQLException {
        String sql = "DELETE FROM Suppliers WHERE supplierId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
        }
    }
}
