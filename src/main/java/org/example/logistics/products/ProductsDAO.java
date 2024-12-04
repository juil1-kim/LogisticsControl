package org.example.logistics.products;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO implements ProductsDAOInterface {
    private Connection conn;

    // Constructor: DatabaseConnection에서 Connection 가져오기
    public ProductsDAO() throws SQLException, ClassNotFoundException {
        this.conn = DatabaseConnection.getConnection();
    }

    // CREATE: 상품 추가
    @Override
    public void addProduct(ProductsVO product) throws SQLException {
        String sql = "INSERT INTO Products (name, description, category_id, price, manufacturer_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getCategoryId());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getManufacturerId());
            stmt.executeUpdate();

            // 로그 기록
            CRUDLogger.log("CREATE", "상품", "상품 추가: " + product.getName());
        } catch (SQLException e) {
            // 실패 시 로그
            CRUDLogger.log("ERROR", "상품", "상품 추가 실패: " + product.getName());
            throw e;
        }
    }

    // READ ALL: 모든 상품 가져오기
    @Override
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

                // 로그 기록
                CRUDLogger.log("READ", "상품", "상품 조회: " + product.getName());
            }
        } catch (SQLException e) {
            // 실패 시 로그
            CRUDLogger.log("ERROR", "상품", "모든 상품 조회 실패");
            throw e;
        }
        return products;
    }

    // READ BY ID: 특정 ID의 상품 가져오기
    @Override
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

                // 로그 기록
                CRUDLogger.log("READ", "상품", "상품 ID로 조회: ID " + productId);
                return product;
            } else {
                CRUDLogger.log("WARN", "상품", "조회된 상품 없음: ID " + productId);
                return null;
            }
        } catch (SQLException e) {
            // 실패 시 로그
            CRUDLogger.log("ERROR", "상품", "상품 ID 조회 실패: ID " + productId);
            throw e;
        }
    }

    // UPDATE: 상품 정보 수정
    @Override
    public void updateProduct(ProductsVO product) throws SQLException {
        String sql = "UPDATE Products SET name = ?, description = ?, category_id = ?, price = ?, manufacturer_id = ? WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getCategoryId());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getManufacturerId());
            stmt.setInt(6, product.getProductId());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                // 로그 기록
                CRUDLogger.log("UPDATE", "상품", "상품 수정: ID " + product.getProductId());
            } else {
                CRUDLogger.log("WARN", "상품", "수정된 상품 없음: ID " + product.getProductId());
            }
        } catch (SQLException e) {
            // 실패 시 로그
            CRUDLogger.log("ERROR", "상품", "상품 수정 실패: ID " + product.getProductId());
            throw e;
        }
    }

    // DELETE: 특정 ID의 상품 삭제
    @Override
    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                // 로그 기록
                CRUDLogger.log("DELETE", "상품", "상품 삭제: ID " + productId);
            } else {
                CRUDLogger.log("WARN", "상품", "삭제된 상품 없음: ID " + productId);
            }
        } catch (SQLException e) {
            // 실패 시 로그
            CRUDLogger.log("ERROR", "상품", "상품 삭제 실패: ID " + productId);
            throw e;
        }
    }
}

