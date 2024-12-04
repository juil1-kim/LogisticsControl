package org.example.logistics.products;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAO implements CategoriesDAOInterface {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public CategoriesDAO() throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE: 카테고리 추가
    @Override
    public void addCategory(CategoriesVO category) {
        String sql = "INSERT INTO Categories (name, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
            CRUDLogger.log("CREATE", "카테고리", "카테고리 추가 성공: " + category.getName());
        } catch (SQLException e) {
            logAndThrow("카테고리 추가 실패: " + category.getName(), e);
        }
    }

    // READ ALL: 모든 카테고리 가져오기
    @Override
    public List<CategoriesVO> getAllCategories() {
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
            CRUDLogger.log("READ", "카테고리", "전체 카테고리 조회 성공");
        } catch (SQLException e) {
            logAndThrow("전체 카테고리 조회 실패", e);
        }
        return categories;
    }

    // READ BY ID: 특정 ID의 카테고리 가져오기
    @Override
    public CategoriesVO getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CategoriesVO category = new CategoriesVO();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                CRUDLogger.log("READ", "카테고리", "카테고리 조회 성공: ID " + categoryId);
                return category;
            } else {
                CRUDLogger.log("WARN", "카테고리", "카테고리 조회 실패: ID " + categoryId + " (데이터 없음)");
            }
        } catch (SQLException e) {
            logAndThrow("카테고리 조회 실패: ID " + categoryId, e);
        }
        return null;
    }

    // UPDATE: 카테고리 정보 수정
    @Override
    public void updateCategory(CategoriesVO category) {
        String sql = "UPDATE Categories SET name = ?, description = ? WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                CRUDLogger.log("UPDATE", "카테고리", "카테고리 수정 성공: ID " + category.getCategoryId());
            } else {
                CRUDLogger.log("WARN", "카테고리", "수정된 카테고리 없음: ID " + category.getCategoryId());
            }
        } catch (SQLException e) {
            logAndThrow("카테고리 수정 실패: ID " + category.getCategoryId(), e);
        }
    }

    // DELETE: 특정 ID의 카테고리 삭제
    @Override
    public void deleteCategory(int categoryId) {
        String sql = "DELETE FROM Categories WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                CRUDLogger.log("DELETE", "카테고리", "카테고리 삭제 성공: ID " + categoryId);
            } else {
                CRUDLogger.log("WARN", "카테고리", "삭제된 카테고리 없음: ID " + categoryId);
            }
        } catch (SQLException e) {
            logAndThrow("카테고리 삭제 실패: ID " + categoryId, e);
        }
    }

    // DELETE ALL: 모든 카테고리 삭제 (주의: 위험 작업)
    @Override
    public void deleteAllCategories() {
        String sql = "DELETE FROM Categories";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
            CRUDLogger.log("DELETE", "카테고리", "모든 카테고리 삭제 성공");
        } catch (SQLException e) {
            logAndThrow("모든 카테고리 삭제 실패", e);
        }
    }

    // Helper Method: 예외를 처리하고 로그 기록 후 다시 예외 던지기
    private void logAndThrow(String message, SQLException e) {
        CRUDLogger.log("ERROR", "카테고리", message + " - " + e.getMessage());
        throw new RuntimeException(message, e);
    }
}

