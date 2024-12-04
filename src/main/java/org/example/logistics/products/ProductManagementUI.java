package org.example.logistics.products;

import java.sql.SQLException;
import java.util.Scanner;

public class ProductManagementUI {
    private Scanner sc;

    public ProductManagementUI() {
        this.sc = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== 상품 정보 관리 ===");
            System.out.println("1. 제품 관리");
            System.out.println("2. 카테고리 관리");
            System.out.println("3. 제조사 관리");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.print("옵션을 선택하세요: ");

            int choice = sc.nextInt();
            sc.nextLine(); // 개행 문자 처리

            try {
                switch (choice) {
                    case 1:
                        manageProductUI();
                        break;
                    case 2:
                        manageCategoryUI();
                        break;
                    case 3:
                        manageManufacturerUI();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                }
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        }
    }

    private void manageProductUI() throws SQLException, ClassNotFoundException {
        ProductsUI productsUI = new ProductsUI();
        productsUI.start();
    }

    private void manageCategoryUI() throws SQLException, ClassNotFoundException {
        CategoriesUI categoriesUI = new CategoriesUI();
        categoriesUI.start();
    }

    private void manageManufacturerUI() throws SQLException, ClassNotFoundException {
        ManufacturersUI manufacturersUI = new ManufacturersUI();
        manufacturersUI.main(null);
    }
}
