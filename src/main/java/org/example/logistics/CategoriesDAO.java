package org.example.logistics;

import org.example.logistics.CategoriesVO;
import org.example.logistics.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAO {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public CategoriesDAO() throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE: 카테고리 추가
    public void addCategory(CategoriesVO category) throws SQLException {
        String sql = "INSERT INTO Categories (name, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
        }
    }

    // READ ALL: 모든 카테고리 가져오기
    public List<CategoriesVO> getAllCategories() throws SQLException {
        List<CategoriesVO> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CategoriesVO category = new CategoriesVO();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        }
        return categories;
    }

    // READ BY ID: 특정 ID의 카테고리 가져오기
    public CategoriesVO getCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CategoriesVO category = new CategoriesVO();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                return category;
            }
        }
        return null;
    }

    // UPDATE: 카테고리 정보 수정
    public void updateCategory(CategoriesVO category) throws SQLException {
        String sql = "UPDATE Categories SET name = ?, description = ? WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());
            stmt.executeUpdate();
        }
    }

    // DELETE: 특정 ID의 카테고리 삭제
    public void deleteCategory(int categoryId) throws SQLException {
        String sql = "DELETE FROM Categories WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
        }
    }

    // DELETE ALL: 모든 카테고리 삭제 (주의: 위험 작업)
    public void deleteAllCategories() throws SQLException {
        String sql = "DELETE FROM Categories";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
