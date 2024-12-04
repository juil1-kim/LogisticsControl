package org.example.logistics.productStatistics;

import org.example.logistics.service.CRUDLogger;
import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierProductDAO implements SupplierProductDAOInterface {
    // 공급자별 공급 제품과 연락처 조회
    @Override
    public List<SupplierProductVO> getSupplierProducts() {
        List<SupplierProductVO> supplierList = new ArrayList<>();
        String query = """
                SELECT s.name AS supplier_name, s.contact, GROUP_CONCAT(p.name SEPARATOR ', ') AS products
                FROM Suppliers s
                JOIN Supplier_Products sp ON s.supplier_id = sp.supplier_id
                JOIN Products p ON sp.product_id = p.product_id
                GROUP BY s.supplier_id;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SupplierProductVO vo = new SupplierProductVO();
                vo.setSupplierName(rs.getString("supplier_name"));
                vo.setContact(rs.getString("contact"));

                // 제품 목록을 리스트로 변환
                String[] productArray = rs.getString("products").split(", ");
                vo.setProducts(List.of(productArray));

                supplierList.add(vo);
            }

            // 성공 로그 기록
            CRUDLogger.log("READ", "공급자-제품", "공급자별 제품 조회 성공");

        } catch (SQLException e) {
            // 실패 로그 기록 및 예외 처리
            CRUDLogger.log("ERROR", "공급자-제품", "공급자별 제품 조회 실패 - " + e.getMessage());
            throw new RuntimeException("공급자별 제품 조회 실패", e);
        }
        return supplierList;
    }
}
