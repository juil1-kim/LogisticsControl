package org.example.logistics.administrators;

import org.example.logistics.branches.BranchesUI;
import org.example.logistics.logViewer.LogViewerUI;
import org.example.logistics.orders.OrdersUI;
import org.example.logistics.products.ProductManagementUI;
import org.example.logistics.suppliers.SuppliersUI;
import org.example.logistics.warehouses.WarehousesUI;


import javax.swing.*;
import java.sql.SQLException;
import java.util.Scanner;

public class AdministratorsLoginUI {
    private AdministratorsDAO loginDAO;
    private Scanner sc;

    public AdministratorsLoginUI() throws SQLException, ClassNotFoundException {
        this.loginDAO = new AdministratorsDAO();
        this.sc = new Scanner(System.in);
    }

    public void start() throws SQLException {
        while (true) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter ID: ");
            String user_id = sc.next();
            sc.nextLine();

            // JPasswordField를 사용하여 비밀번호 마스킹
            String password = getPasswordFromDialog("Enter password: ");

            AdministratorsVO authority = loginDAO.login(user_id, password);

            if (authority != null) {
                if ("root".equals(authority.getRole())) {
                    System.out.println("\nRoot administrator 로그인 성공.");
                    rootMode();
                } else if ("general".equals(authority.getRole())) {
                    System.out.println("\nGeneral administrator 로그인 성공");
                    generalMode();
                } else {
                    System.out.println("해당 계정에 주어진 권한이 없습니다.");
                }
            } else {
                System.out.println("아이디나 비밀번호가 올바르지 않습니다.");
                // 비밀번호 찾기 기능 구현
            }
        }
    }

    private String getPasswordFromDialog(String message) {
        JPasswordField jpf = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(null, jpf, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return new String(jpf.getPassword());
        } else {
            return null;
        }
    }

    private void rootMode() {
        while (true) {
            System.out.println("\n=== Root Mode ===");
            System.out.println("1. 상품 관리");
            System.out.println("2. 주문 관리");
            System.out.println("3. 창고 관리");
            System.out.println("4. 지점 관리");
            System.out.println("5. 공급자 관리");
            System.out.println("6. 일반 관리자 관리");
            System.out.println("9. 로그 기록 확인");
            System.out.println("0. 로그아웃");
            System.out.println("-1. 프로그램 종료");
            System.out.print("메뉴를 선택하세요 >> ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    // 상품 관리
                    manageAllProductInfo();
                    break;
                case 2:
                    // 주문 관리
                    OrdersUI.warehouseFunction();
                    break;
                case 3:
                    // 창고 관리
                    WarehousesUI.function();
                    break;
                case 4:
                    // 지점 관리
                    BranchesUI.manegeBranches();
                    break;
                case 5:
                    // 공급자 관리
                    SuppliersUI.function();
                    break;
                case 6:
                    // 일반 관리자 관리
                    AdministratorsUI.manegeGeneralAdmin();
                    break;
                case 9:
                    launchLogViewer(); // 로그 뷰어 실행
                    break;
                case 0:
                    System.out.println("로그아웃");
                    return;
                case -1:
                    System.out.println("프로그램 종료");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("메뉴를 다시 선택하세요");
            }
        }
    }

    private void generalMode() {
        while (true) {
            System.out.println("\n=== General Mode ===");
            System.out.println("1. 상품 관리");
            System.out.println("2. 주문 요청");
            System.out.println("0. 로그아웃");
            System.out.println("-1. 프로그램 종료");
            System.out.print("메뉴를 선택하세요 >> ");

            int choice = sc.nextInt();
            sc.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    // 주문 관리
                    manageAllProductInfo();
                    break;
                case 2:
                    // 주문 요청
                    OrdersUI.branchFunction();
                    break;
                case 0:
                    System.out.println("로그아웃");
                    return;
                case -1:
                    System.out.println("프로그램을 종료합니다.");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("메뉴를 다시 선택하시오");
            }
        }
    }

    // 근영 추가
    private void manageAllProductInfo() {
        ProductManagementUI productManagementUI = new ProductManagementUI();
        productManagementUI.start();
    }

    private void launchLogViewer() {
        SwingUtilities.invokeLater(() -> new LogViewerUI().createAndShowGUI());
    }
// 여기 까지

    public static void main(String[] args) {
        try{
            AdministratorsLoginUI ui = new AdministratorsLoginUI();
            ui.start();
        }catch(Exception e){
            System.out.println("프로그램을 실행하지 못했습니다.\nError: " + e.getMessage());
        }
    }
}