package org.example.logistics.productStatistics;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryProductManufacturerDAO {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public CategoryProductManufacturerDAO(Connection connection) throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // 카테고리, 상품, 제조사 정보를 가져오는 메서드
    public List<CategoryProductManufacturerVO> getCategoryProductManufacturers() {
        String query = """
            SELECT c.name AS categoryName, p.name AS productName,
                   m.name AS manufacturerName, m.contact AS manufacturerContact
            FROM Products p
            JOIN Categories c ON p.category_id = c.category_id
            LEFT JOIN Manufacturers m ON p.manufacturer_id = m.manufacturer_id
            ORDER BY c.name, p.name
        """;

        List<CategoryProductManufacturerVO> result = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new CategoryProductManufacturerVO(
                        rs.getString("categoryName"),
                        rs.getString("productName"),
                        rs.getString("manufacturerName"),
                        rs.getString("manufacturerContact")
                ));
            }

            // 성공 로그 기록
            CRUDLogger.log("READ", "카테고리-상품-제조사", "카테고리, 상품, 제조사 조회 성공");

        } catch (SQLException e) {
            // 실패 로그 기록 및 예외 처리
            CRUDLogger.log("ERROR", "카테고리-상품-제조사", "카테고리, 상품, 제조사 조회 실패 - " + e.getMessage());
            throw new RuntimeException("카테고리, 상품, 제조사 조회 실패", e);
        }

        return result;
    }
}
