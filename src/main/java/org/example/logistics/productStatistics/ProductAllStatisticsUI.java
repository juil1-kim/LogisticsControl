package org.example.logistics.productStatistics;

import org.example.logistics.service.DatabaseConnection;

import java.util.Scanner;

public class ProductAllStatisticsUI {
    private final Scanner scanner;

    public ProductAllStatisticsUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            while (true) {
                System.out.println("\n=== 상품 관련 세부 정보 ===");
                System.out.println("1. 제품 재고 목록 및 통계");
                System.out.println("2. 카테고리별 제품과 제조사 보기");
                System.out.println("3. 제품별 공급자 정보 조회");
                System.out.println("0. 이전 메뉴로 돌아가기");
                System.out.print("옵션을 선택하세요: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // 개행 문자 처리

                switch (choice) {
                    case 1:
                        // ProductInventoryDAO 객체 생성 및 전달
                        ProductInventoryDAOInterface productInventoryDAO = new ProductInventoryDAO();
                        manageProductInventory(productInventoryDAO);
                        break;
                    case 2:
                        // CateProManuDAO 객체 생성 및 전달
                        CateProManuDAOInterface categoryProductDAO =
                                new CategoryProductManufacturerDAO(DatabaseConnection.getConnection());
                        viewCategoryProductManufacturerStatistics(categoryProductDAO);
                        break;
                    case 3:
                        // SupplierProductDAO 객체 생성 및 전달
                        SupplierProductDAOInterface supplierProductDAO = new SupplierProductDAO();
                        viewSupplierProductStatistics(supplierProductDAO);
                        break;
                    case 0:
                        System.out.println("메인 메뉴로 돌아갑니다.");
                        return;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 시도하세요.");
                }
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        } finally {
            // 데이터베이스 연결 종료
            DatabaseConnection.close();
        }
    }

    private void manageProductInventory(ProductInventoryDAOInterface productInventoryDAO) {
        ProductInventoryUI inventoryUI = new ProductInventoryUI(productInventoryDAO);
        inventoryUI.start();
    }

    private void viewCategoryProductManufacturerStatistics(CateProManuDAOInterface categoryProductDAO) {
        CategoryProductManufacturerUI categoryUI = new CategoryProductManufacturerUI(categoryProductDAO);
        categoryUI.start();
    }

    private void viewSupplierProductStatistics(SupplierProductDAOInterface supplierProductDAO) {
        SupplierProductUI supplierUI = new SupplierProductUI(supplierProductDAO);
        supplierUI.displaySuppliers(); // 공급자 통계 관리
    }
}
