package org.example.logistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public ProductsDAO() throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE: 상품 추가
    public void addProduct(ProductsVO product) throws SQLException {
        // created_at은 DEFAULT CURRENT_TIMESTAMP로 자동 설정
        String sql = "INSERT INTO Products (name, description, category_id, price, manufacturer_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getCategoryId());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getManufacturerId());
            stmt.executeUpdate();
        }
    }

    // READ ALL: 모든 상품 가져오기
    public List<ProductsVO> getAllProducts() throws SQLException {
        List<ProductsVO> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProductsVO product = new ProductsVO();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setPrice(rs.getDouble("price"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setManufacturerId(rs.getInt("manufacturer_id"));
                products.add(product);
            }
        }
        return products;
    }

    // READ BY ID: 특정 ID의 상품 가져오기
    public ProductsVO getProductById(int productId) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ProductsVO product = new ProductsVO();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setPrice(rs.getDouble("price"));
                product.setCreatedAt(rs.getString("created_at"));
                product.setManufacturerId(rs.getInt("manufacturer_id"));
                return product;
            }
        }
        return null;
    }

    // UPDATE: 상품 정보 수정
    public void updateProduct(ProductsVO product) throws SQLException {
        // created_at 필드는 수정하지 않음
        String sql = "UPDATE Products SET name = ?, description = ?, category_id = ?, price = ?, manufacturer_id = ? WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getCategoryId());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getManufacturerId());
            stmt.setInt(6, product.getProductId());
            stmt.executeUpdate();
        }
    }

    // DELETE: 특정 ID의 상품 삭제
    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }
}
