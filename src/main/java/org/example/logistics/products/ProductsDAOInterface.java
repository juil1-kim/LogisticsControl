package org.example.logistics.products;

import java.sql.SQLException;
import java.util.List;

public interface ProductsDAOInterface {
    // CREATE: 상품 추가
    void addProduct(ProductsVO product) throws SQLException;

    // READ ALL: 모든 상품 가져오기
    List<ProductsVO> getAllProducts() throws SQLException;

    // READ BY ID: 특정 ID의 상품 가져오기
    ProductsVO getProductById(int productId) throws SQLException;

    // UPDATE: 상품 정보 수정
    void updateProduct(ProductsVO product) throws SQLException;

    // DELETE: 특정 ID의 상품 삭제
    void deleteProduct(int productId) throws SQLException;
}
