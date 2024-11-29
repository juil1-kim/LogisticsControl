package org.example.logistics.productStatistics;

import org.example.logistics.DatabaseConnection;


import java.util.List;

public class SupplierProductUI {
    public static void main(String[] args) {
        SupplierProductDAO dao = new SupplierProductDAO();
        List<SupplierProductVO> suppliers = dao.getSupplierProducts();

        System.out.println("공급자별 공급 제품 목록");
        System.out.println("=============================");

        if (suppliers.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            for (SupplierProductVO supplier : suppliers) {
                System.out.println("공급자: " + supplier.getSupplierName());
                System.out.println("연락처: " + supplier.getContact());
                System.out.println("공급 제품: " + String.join(", ", supplier.getProducts()));
                System.out.println("-----------------------------");
            }
        }

        // 데이터베이스 연결 종료
        DatabaseConnection.close();
    }
}

