package org.example.logistics.administrators;

import java.sql.SQLException;
import java.util.Scanner;

public class AdministratorsPasswordUpdateUI {
    private AdministratorsDAO administratorsDAO;
    private Scanner scanner;

    public AdministratorsPasswordUpdateUI() throws SQLException {
        this.administratorsDAO = new AdministratorsDAO();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== 관리자 비밀번호 업데이트 ===");
            System.out.println("1. 모든 비밀번호를 BCrypt 해시로 업데이트");
            System.out.println("0. 종료");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 개행 문자 소비

            switch (choice) {
                case 1:
                    updatePasswords();
                    break;
                case 0:
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도하세요.");
            }
        }
    }

    private void updatePasswords() {
        try {
            administratorsDAO.updatePasswordsToBCrypt();
            System.out.println("비밀번호가 성공적으로 업데이트되었습니다.");
        } catch (SQLException e) {
            System.out.println("비밀번호 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            AdministratorsPasswordUpdateUI ui = new AdministratorsPasswordUpdateUI();
            ui.start();
        } catch (SQLException e) {
            System.out.println("데이터베이스 연결 오류: " + e.getMessage());
        }
    }
}