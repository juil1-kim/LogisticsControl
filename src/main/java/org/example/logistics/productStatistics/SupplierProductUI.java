package org.example.logistics.productStatistics;

import org.example.logistics.service.DatabaseConnection;

import java.util.List;

public class SupplierProductUI {
    // private final : 객체 참조 불변성 보장
    private final SupplierProductDAOInterface dao;

    // 생성자: DAO 인터페이스 구현체를 주입받음
    public SupplierProductUI(SupplierProductDAOInterface dao) {
        this.dao = dao;
    }

    public void displaySuppliers() {
        List<SupplierProductVO> suppliers = dao.getSupplierProducts();

        System.out.println("=".repeat(90));
        System.out.printf("%-20s | %-20s | %-20s%n", "공급 제품", "공급자", "연락처");
        System.out.println("-".repeat(90));

        if (suppliers.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            for (SupplierProductVO supplier : suppliers) {
                System.out.printf(
                        "%-20s | %-20s | %-20s%n",
                        String.join(", ", supplier.getProducts()),
                        supplier.getSupplierName(),
                        supplier.getContact()
                );
            }
        }

        System.out.println("=".repeat(90));
    }

    public static void main(String[] args) {
        try {
            // Connection 객체 생성
            SupplierProductDAOInterface dao = new SupplierProductDAO();
            // UI 객체 생성 및 실행
            SupplierProductUI ui = new SupplierProductUI(dao);
            ui.displaySuppliers();
        } finally {
            // 데이터베이스 연결 종료
            DatabaseConnection.close();
        }
    }
}
