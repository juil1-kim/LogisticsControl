package org.example.logistics.productStatistics;

import java.util.Scanner;

public class ProductAllStatisticsUI {
    private final Scanner scanner;

    public ProductAllStatisticsUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== 상품 관련 세부 정보 ===");
            System.out.println("1. 제품 재고 관리");
            System.out.println("2. 카테고리-제품-제조사 통계");
            System.out.println("3. 제품별 공급자 정보 조회");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.print("옵션을 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1:
                    manageProductInventory();
                    break;
                case 2:
                    viewCategoryProductManufacturerStatistics();
                    break;
                case 3:
                    viewSupplierProductStatistics();
                    break;
                case 0:
                    System.out.println("메인 메뉴로 돌아갑니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
            }
        }
    }

    private void manageProductInventory() {
        ProductInventoryUI inventoryUI = new ProductInventoryUI();
        inventoryUI.start();
    }

    private void viewCategoryProductManufacturerStatistics() {
        CategoryProductManufacturerUI categoryUI = new CategoryProductManufacturerUI();
        categoryUI.start();
    }

    private void viewSupplierProductStatistics() {
        SupplierProductUI supplierUI = new SupplierProductUI();
        supplierUI.main(null); // 공급자 통계 관리
    }
}
