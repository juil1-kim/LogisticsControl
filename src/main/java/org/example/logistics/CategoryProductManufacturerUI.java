package org.example.logistics;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CategoryProductManufacturerUI {
    private final CategoryProductManufacturerDAO dao;

    public CategoryProductManufacturerUI() {
        // DAO 초기화
        try {
            Connection connection = DatabaseConnection.getConnection();
            dao = new CategoryProductManufacturerDAO(connection);
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("========== Category-Product-Manufacturer ==========");
            System.out.println("1. 데이터 보기");
            System.out.println("2. 종료");
            System.out.print("선택: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loadTableData();
                    break;
                case "2":
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
            }
        }
    }

    private void loadTableData() {
        try {
            // DAO에서 데이터 가져오기
            List<CategoryProductManufacturerVO> data = dao.getCategoryProductManufacturers();

            // 테이블 출력
            System.out.println("=======================================================");
            System.out.printf("%-20s %-20s %-20s %-15s%n",
                    "Category Name", "Product Name", "Manufacturer Name", "Contact");
            System.out.println("=======================================================");
            for (CategoryProductManufacturerVO vo : data) {
                System.out.printf("%-20s %-20s %-20s %-15s%n",
                        vo.getCategoryName(),
                        vo.getProductName(),
                        vo.getManufacturerName(),
                        vo.getManufacturerContact());
            }
            System.out.println("=======================================================");
        } catch (Exception e) {
            System.out.println("데이터 로드 실패: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CategoryProductManufacturerUI().start();
    }
}
