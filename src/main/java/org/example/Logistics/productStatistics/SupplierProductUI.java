package org.example.logistics.productStatistics;

import org.example.logistics.DatabaseConnection;

import java.util.List;

public class SupplierProductUI {
    public static void main(String[] args) {
        SupplierProductDAO dao = new SupplierProductDAO();
        List<SupplierProductVO> suppliers = dao.getSupplierProducts();

        System.out.println("=".repeat(90));
        System.out.printf("%-10s | %-10s | %-20s%n", "공급 제품", "공급자", "연락처");
        System.out.println("-".repeat(90));

        if (suppliers.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            for (SupplierProductVO supplier : suppliers) {
                System.out.printf(
                        "%-10s | %-10s | %-20s%n",
                        String.join(", ", supplier.getProducts()),
                        supplier.getSupplierName(),
                        supplier.getContact()
                );
            }
        }

        System.out.println("=".repeat(90));

        // 데이터베이스 연결 종료
        DatabaseConnection.close();
    }
}

