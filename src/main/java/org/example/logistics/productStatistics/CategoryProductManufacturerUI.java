package org.example.logistics.productStatistics;

import org.example.logistics.service.DatabaseConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CategoryProductManufacturerUI {
    private final CateProManuDAOInterface dao;

    // 생성자: DAO 인터페이스를 주입받음
    public CategoryProductManufacturerUI(CateProManuDAOInterface dao) {
        this.dao = dao;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("========== Category-Product-Manufacturer ==========");
            System.out.println("1. 데이터 보기");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loadTableData();
                    break;
                case "0":
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
        try {
            // Connection 객체 생성
            Connection connection = DatabaseConnection.getConnection();
            // 인터페이스 구현체 생성 및 주입
            CateProManuDAOInterface dao = new CategoryProductManufacturerDAO(connection);
            // UI 시작
            new CategoryProductManufacturerUI(dao).start();
        } catch (Exception e) {
            System.out.println("프로그램 시작에 실패했습니다: " + e.getMessage());
        }
    }
}
