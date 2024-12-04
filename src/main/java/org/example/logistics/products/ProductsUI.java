package org.example.logistics.products;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductsUI {
    private final ProductsDAOInterface productsDAO; // 인터페이스 참조
    private final Scanner scanner;

    // 생성자: 인터페이스 구현체 주입
    public ProductsUI(ProductsDAOInterface productsDAO) {
        this.productsDAO = productsDAO;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== 제품 관리 시스템 ===");
            System.out.println("1. 제품 추가");
            System.out.println("2. 모든 제품 조회");
            System.out.println("3. ID로 제품 조회");
            System.out.println("4. 제품 정보 수정");
            System.out.println("5. 제품 삭제");
            System.out.println("0. 이전 메뉴로 돌아가기");
            System.out.print("옵션을 선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            try {
                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        viewAllProducts();
                        break;
                    case 3:
                        viewProductById();
                        break;
                    case 4:
                        updateProduct();
                        break;
                    case 5:
                        deleteProduct();
                        break;
                    case 0:
                        System.out.println("종료 중...");
                        return;
                    default:
                        System.out.println("잘못된 선택입니다. 다시 시도하세요.");
                }
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        }
    }

    private void addProduct() throws SQLException {
        System.out.print("제품 이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("제품 설명을 입력하세요: ");
        String description = scanner.nextLine();
        System.out.print("카테고리 ID를 입력하세요: ");
        int categoryId = scanner.nextInt();
        System.out.print("가격을 입력하세요: ");
        double price = scanner.nextDouble();
        System.out.print("제조사 ID를 입력하세요: ");
        int manufacturerId = scanner.nextInt();

        ProductsVO product = new ProductsVO();
        product.setName(name);
        product.setDescription(description);
        product.setCategoryId(categoryId);
        product.setPrice(price);
        product.setManufacturerId(manufacturerId);

        productsDAO.addProduct(product);
        System.out.println("제품이 성공적으로 추가되었습니다!");
    }

    private void viewAllProducts() throws SQLException {
        List<ProductsVO> products = productsDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("등록된 제품이 없습니다.");
        } else {
            System.out.println("\n=== 제품 목록 ===");
            for (ProductsVO product : products) {
                System.out.println("ID: " + product.getProductId());
                System.out.println("이름: " + product.getName());
                System.out.println("설명: " + product.getDescription());
                System.out.println("카테고리 ID: " + product.getCategoryId());
                System.out.println("가격: " + product.getPrice());
                System.out.println("생성일: " + product.getCreatedAt());
                System.out.println("제조사 ID: " + product.getManufacturerId());
                System.out.println("----------------------");
            }
        }
    }

    private void viewProductById() throws SQLException {
        System.out.print("조회할 제품 ID를 입력하세요: ");
        int productId = scanner.nextInt();

        ProductsVO product = productsDAO.getProductById(productId);
        if (product == null) {
            System.out.println("해당 ID의 제품을 찾을 수 없습니다.");
        } else {
            System.out.println("\n=== 제품 상세 정보 ===");
            System.out.println("ID: " + product.getProductId());
            System.out.println("이름: " + product.getName());
            System.out.println("설명: " + product.getDescription());
            System.out.println("카테고리 ID: " + product.getCategoryId());
            System.out.println("가격: " + product.getPrice());
            System.out.println("생성일: " + product.getCreatedAt());
            System.out.println("제조사 ID: " + product.getManufacturerId());
        }
    }

    private void updateProduct() throws SQLException {
        System.out.print("수정할 제품의 ID를 입력하세요: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리

        System.out.print("새 이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("새 설명을 입력하세요: ");
        String description = scanner.nextLine();
        System.out.print("새 카테고리 ID를 입력하세요: ");
        int categoryId = scanner.nextInt();
        System.out.print("새 가격을 입력하세요: ");
        double price = scanner.nextDouble();
        System.out.print("새 제조사 ID를 입력하세요: ");
        int manufacturerId = scanner.nextInt();

        ProductsVO product = new ProductsVO();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setCategoryId(categoryId);
        product.setPrice(price);
        product.setManufacturerId(manufacturerId);

        productsDAO.updateProduct(product);
        System.out.println("제품 정보가 성공적으로 수정되었습니다!");
    }

    private void deleteProduct() throws SQLException {
        System.out.print("삭제할 제품의 ID를 입력하세요: ");
        int productId = scanner.nextInt();

        productsDAO.deleteProduct(productId);
        System.out.println("제품이 성공적으로 삭제되었습니다!");
    }

    public static void main(String[] args) {
        try {
            // 인터페이스 구현체 생성 후 주입
            ProductsDAOInterface productsDAO = new ProductsDAO();
            ProductsUI ui = new ProductsUI(productsDAO);
            ui.start();
        } catch (Exception e) {
            System.out.println("프로그램 시작에 실패했습니다: " + e.getMessage());
        }
    }
}

