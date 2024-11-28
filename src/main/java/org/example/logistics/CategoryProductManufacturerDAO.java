package org.example.logistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryProductManufacturerDAO {
    private Connection conn;

    public CategoryProductManufacturerDAO(Connection connection) throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public List<CategoryProductManufacturerVO> getCategoryProductManufacturers() throws SQLException {
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
        }
        return result;
    }
}

