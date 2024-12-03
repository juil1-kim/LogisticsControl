package org.example.logistics.branches;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BranchesUI {
    private static BranchesDAO branchesDAO;
    private static Scanner scanner;

    public BranchesUI() throws SQLException, ClassNotFoundException {
        this.branchesDAO = new BranchesDAO();
        this.scanner = new Scanner(System.in);

    }

    public void start() {
        while (true) {
            System.out.println("\n=== 가맹점 정보 관리 ===");
            System.out.println("1. 가맹점 추가");
            System.out.println("2. 전체 가맹점 목록");
            System.out.println("3. 가맹점 검색");
            System.out.println("4. 가맹점 수정");
            System.out.println("5. 가맹점 삭제");
            System.out.println("6. 지점별 총 판매량");
            System.out.println("7. 가맹점 이름순 정렬 목록");
            System.out.println("8. 상품별 가맹점 판매량");
            System.out.println("0. 이전");
            System.out.print("메뉴를 선택하시오: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        addBranch();
                        break;
                    case 2:
                        viewAllBranches();
                        break;
                    case 3:
                        viewBranchById();
                        break;
                    case 4:
                        updateBranch();
                        break;
                    case 5:
                        deleteBranch();
                        break;
                    case 6:
                        sortingBranchSales();
                        break;
                    case 7:
                        sortingBranchNames();
                        break;
                    case 8:
                        sortingBranchProduct();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }



    private void addBranch() throws SQLException {
        System.out.print("Enter branch name: ");
        String name = scanner.nextLine();
        System.out.print("Enter branch location: ");
        String location = scanner.nextLine();

        BranchesVO branch = new BranchesVO();
        branch.setName(name);
        branch.setLocation(location);
        branchesDAO.addBranch(branch);

        System.out.println("Branch added successfully!");
    }

    private void viewAllBranches() throws SQLException {
        List<BranchesVO> branches = branchesDAO.getAllBranches();
        if (branches.isEmpty()) {
            System.out.println("No branches found.");
        } else {
            System.out.println("\n=== Branches ===");
            for (BranchesVO branch : branches) {
                System.out.println("ID: " + branch.getBranchId() +
                        ", Name: " + branch.getName() +
                        ", Location: " + branch.getLocation());
            }
        }
    }

    private void viewBranchById() throws SQLException {
        System.out.print("Enter branch ID: ");
        int id = scanner.nextInt();

        BranchesVO branch = branchesDAO.getBranchById(id);
        if (branch == null) {
            System.out.println("Branch not found.");
        } else {
            System.out.println("ID: " + branch.getBranchId());
            System.out.println("Name: " + branch.getName());
            System.out.println("Location: " + branch.getLocation());
        }
    }

    private void updateBranch() throws SQLException {
        System.out.print("Enter branch ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new location: ");
        String location = scanner.nextLine();

        BranchesVO branch = new BranchesVO();
        branch.setBranchId(id);
        branch.setName(name);
        branch.setLocation(location);
        branchesDAO.updateBranch(branch);

        System.out.println("Branch updated successfully!");
    }

    private void deleteBranch() throws SQLException {
        System.out.print("Enter branch ID to delete: ");
        int id = scanner.nextInt();

        branchesDAO.deleteBranch(id);
        System.out.println("Branch deleted successfully!");
    }

    public static void sortingBranchSales() throws SQLException{

        List<BranchesOutgoingOrdersVO> branches = branchesDAO.sortingBranchSales();

        if (branches.isEmpty()) {
            System.out.println("No branches found.");
        } else {
            System.out.println("\n=== 지점별 총 판매량 순 정렬 ===");
            for (BranchesOutgoingOrdersVO branch : branches) {
                System.out.println("지점 이름: " + branch.getName() +
                        ", 총 판매량: " + branch.getQuantity());
            }
        }
    }

    public static void sortingBranchNames() throws SQLException {
        List<BranchesVO> branches = branchesDAO.sortingBranchNames();

        if (branches.isEmpty()) {
            System.out.println("No branches found.");
        }else {
            System.out.println("\n=== 지점 이름 순 정렬 ===");
            for (BranchesVO branch : branches) {
                System.out.println("ID: " + branch.getBranchId() +
                        ", Name: " + branch.getName() +
                        ", Location: " + branch.getLocation());
            }
        }
    }

    public static void sortingBranchProduct() throws SQLException {
        System.out.print("검색하고자 하는 상품 ID: ");
        int productId = scanner.nextInt(); // 사용자로부터 상품 ID 입력받기
        List<BranchesOutgoingOrdersProductsVO> branches = branchesDAO.sortingBranchProduct(productId);

        if (branches.isEmpty()) {
            System.out.println("No branches found.");
        } else {
            System.out.println("\n=== 상품별 가맹점 판매량 ===");
            for (BranchesOutgoingOrdersProductsVO branch : branches) {
                System.out.println("지점 이름: " + branch.getBranch_name() +
                        ", 상품 이름: " + branch.getProduct_name() +
                        ", 총 판매량: " + branch.getQuantity());
            }
        }
    }

    public static void function() {
        try {
            BranchesUI ui = new BranchesUI();
            ui.start();
        } catch (Exception e) {
            System.out.println("Failed to start application: " + e.getMessage());
        }
    }
}