package org.example.logistics.administrators;

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

            System.out.print("Enter password: ");
            String password = sc.nextLine();

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

    private void rootMode() {
        while (true) {
            System.out.println("\n=== Root Mode ===");
            System.out.println("1. 상품 정보 관리");
            System.out.println("2. 상품 관련 세부 정보");
            System.out.println("3. 창고별 주문 내역 확인");
            System.out.println("4. 가맹점 정보 관리");
            System.out.println("5. 수입 및 지출 관리");
            System.out.println("6. 출고 등록");
            System.out.println("7. 일반 모드 권한 관리");
            System.out.println("0. 로그인 창 이동");
            System.out.println("-1. 프로그램 종료");
            System.out.print("메뉴를 선택하세요 >> ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    // 상품 정보 관리 기능 호출
                    break;
                case 2:
                    // 상품 관련 세부 정보 기능 호출
                    break;
                case 3:
                    // 창고별 주문 내역 확인 기능 호출
                    break;
                case 4:
                    // 가맹점 정보 관리 기능 호출
                    break;
                case 5:
                    // 수입 및 지출 관리 기능 호출
                    break;
                case 6:
                    // 출고 등록 기능 호출
                    break;
                case 7:
                    // 일반 모드 권한 관리 기능 호출
                    break;
                case 0:
                    System.out.println("로그인 창 이동");
                    return;
                case -1:
                    System.out.println("프로그램 종료");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("메뉴를 다시 선택하시오");
            }
        }
    }

    private void generalMode() {
        while (true) {
            System.out.println("\n=== General Mode ===");
            System.out.println("1. 주문 조회");
            System.out.println("2. 상품 정보 관리");
            System.out.println("3. 주문 관리");
            System.out.println("4. 출고 등록");
            System.out.println("0. 로그인 창 이동");
            System.out.println("-1. 프로그램 종료");
            System.out.print("메뉴를 선택하세요 >> ");

            int choice = sc.nextInt();
            sc.nextLine();  // Consume newline character

            switch (choice) {
                case 1:
                    // 주문 조회 기능 호출
                    break;
                case 2:
                    // 상품 정보 관리 기능 호출
                    break;
                case 3:
                    // 주문 관리 기능 호출
                    break;
                case 4:
                    // 출고 등록 기능 호출
                    break;
                case 0:
                    System.out.println("로그인 창 이동");
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

    public static void main(String[] args) {
        try{
            AdministratorsLoginUI ui = new AdministratorsLoginUI();
            ui.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
