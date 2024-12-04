package org.example.logistics.products;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CategoriesUI {
    private final CategoriesDAOInterface categoriesDAO; // 인터페이스 참조

    // Constructor: 인터페이스 타입으로 DAO 객체 받기
    public CategoriesUI(CategoriesDAOInterface categoriesDAO) {
        this.categoriesDAO = categoriesDAO;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== 카테고리 관리 ===");
            System.out.println("1. 카테고리 추가");
            System.out.println("2. 모든 카테고리 조회");
            System.out.println("3. ID로 카테고리 조회");
            System.out.println("4. 카테고리 수정");
            System.out.println("5. 카테고리 삭제");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.print("옵션을 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            try {
                switch (choice) {
                    case 1:
                        addCategory(scanner);
                        break;
                    case 2:
                        viewAllCategories();
                        break;
                    case 3:
                        viewCategoryById(scanner);
                        break;
                    case 4:
                        updateCategory(scanner);
                        break;
                    case 5:
                        deleteCategory(scanner);
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

    private void addCategory(Scanner scanner) throws SQLException {
        System.out.print("카테고리 이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("카테고리 설명을 입력하세요: ");
        String description = scanner.nextLine();

        CategoriesVO category = new CategoriesVO();
        category.setName(name);
        category.setDescription(description);

        categoriesDAO.addCategory(category);
        System.out.println("카테고리가 성공적으로 추가되었습니다!");
    }

    private void viewAllCategories() throws SQLException {
        List<CategoriesVO> categories = categoriesDAO.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("등록된 카테고리가 없습니다.");
        } else {
            System.out.println("\n=== 모든 카테고리 ===");
            for (CategoriesVO category : categories) {
                System.out.println("ID: " + category.getCategoryId() +
                        ", 이름: " + category.getName() +
                        ", 설명: " + category.getDescription());
            }
        }
    }

    private void viewCategoryById(Scanner scanner) throws SQLException {
        System.out.print("조회할 카테고리 ID를 입력하세요: ");
        int id = scanner.nextInt();

        CategoriesVO category = categoriesDAO.getCategoryById(id);
        if (category != null) {
            System.out.println("\n카테고리 상세 정보:");
            System.out.println("ID: " + category.getCategoryId());
            System.out.println("이름: " + category.getName());
            System.out.println("설명: " + category.getDescription());
        } else {
            System.out.println("해당 ID의 카테고리를 찾을 수 없습니다.");
        }
    }

    private void updateCategory(Scanner scanner) throws SQLException {
        System.out.print("수정할 카테고리 ID를 입력하세요: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리

        CategoriesVO category = categoriesDAO.getCategoryById(id);
        if (category != null) {
            System.out.println("현재 이름: " + category.getName());
            System.out.print("새 이름을 입력하세요: ");
            String newName = scanner.nextLine();

            System.out.println("현재 설명: " + category.getDescription());
            System.out.print("새 설명을 입력하세요: ");
            String newDescription = scanner.nextLine();

            category.setName(newName);
            category.setDescription(newDescription);

            categoriesDAO.updateCategory(category);
            System.out.println("카테고리가 성공적으로 수정되었습니다!");
        } else {
            System.out.println("해당 ID의 카테고리를 찾을 수 없습니다.");
        }
    }

    private void deleteCategory(Scanner scanner) throws SQLException {
        System.out.print("삭제할 카테고리 ID를 입력하세요: ");
        int id = scanner.nextInt();

        categoriesDAO.deleteCategory(id);
        System.out.println("카테고리가 성공적으로 삭제되었습니다!");
    }

    public static void main(String[] args) {
        try {
            CategoriesDAOInterface categoriesDAO = new CategoriesDAO(); // 구체적인 구현체 주입
            CategoriesUI ui = new CategoriesUI(categoriesDAO);
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}