package org.example.logistics.administrators;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdministratorsUI {
    private AdministratorsDAO administratorsDAO;
    private Scanner sc;

    public AdministratorsUI() throws SQLException {
        this.administratorsDAO = new AdministratorsDAO();
        this.sc = new Scanner(System.in);
    }

    public void start(){
        while (true) {
            System.out.println("\n===== 일반 관리자 권한 관리 =====");
            System.out.println("1. 일반 관리자 계정 생성");
            System.out.println("2. 일반 관리자 목록");
            System.out.println("3. 일반 관리자 검색");
            System.out.println("4. 일반 관리자 ID 변경");
            System.out.println("5. 일반 관리자 계정 삭제");
            System.out.println("0. Exit");
            System.out.print("메뉴를 선택하시오: ");

            int choice = sc.nextInt();
            sc.nextLine();
            try{
                switch (choice) {
                    case 1:
                        addAdministrators();
                        break;
                    case 2:
                        viewAllAdministrators();
                        break;
                    case 3:
                        viewAdministratorsByID();
                        break;
                    case 4:
                        updateAdministrator();
                        break;
                    case 5:
                        deleteAdministrator();
                        break;
                    case 0:
                        System.out.println("이전 화면으로 돌아갑니다.");
                        return;
                    default:
                        System.out.println("올바른 메뉴를 선택하시오.");
                }

            }catch (Exception e){
                System.out.println("에러 메세지: " + e.getMessage());
            }
        }
    }

    private void addAdministrators() throws SQLException {
        System.out.print("아이디: ");
        String user_id = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();

        AdministratorsVO administrator = new AdministratorsVO();
        administrator.setUser_id(user_id);
        administrator.setPassword(password);
        administrator.setRole("General");
        administratorsDAO.addAdministrator(administrator);
        administratorsDAO.updatePasswordsToBCrypt();

        System.out.println("성공적으로 일반 유저 추가가 완료되었습니다.");
    }

    private void viewAllAdministrators() throws SQLException {
        List<AdministratorsVO> administrators = administratorsDAO.viewAllAdministrators();
        if (administrators.isEmpty()) {
            System.out.println("유저가 없습니다.");
        }else{
            System.out.println("\n====== 전체 일반 관리자 목록 ======");
            System.out.println("---------------------------------");
            System.out.println("| 일반 관리자 고유 ID | 일반 관리자 ID |");

            for (AdministratorsVO administrator : administrators) {
                System.out.println("----------------------------------");
                System.out.println("|\t\t " + administrator.getAdmin_id() + " \t\t | \t" + administrator.getUser_id() + "  \t|");
            }
        }
    }

    private void viewAdministratorsByID() throws SQLException {
        System.out.print("일반 관리자 ID: ");
        String user_id = sc.nextLine();

        AdministratorsVO administrator = administratorsDAO.viewAdministratorById(user_id);
        if (administrator == null) {
            System.out.println("해당 유저를 찾을 수 없습니다.");
        }else{
            System.out.println("일반 관리자 고유 ID: " + administrator.getAdmin_id());
            System.out.println("일반 관리자 ID: " + administrator.getUser_id());
            System.out.println("비밀번호: " + administrator.getPassword());
        }
    }

    private void updateAdministrator() throws SQLException {
        System.out.print("변경할 일반 관리자 고유 ID: ");
        int admin_id = sc.nextInt();
        sc.nextLine();

        System.out.print("새로운 일반 관리자 ID: ");
        String user_id = sc.nextLine();

        AdministratorsVO administrator = new AdministratorsVO();
        administrator.setAdmin_id(admin_id);
        administrator.setUser_id(user_id);
        administrator.setRole("general");

        administratorsDAO.updateAdministrator(administrator);
        System.out.println("'" + user_id +"'로 ID가 정상 변경 되었습니다.");

    }

    private void deleteAdministrator() throws SQLException {
        System.out.print("삭제할 관리자 ID: ");
        String user_id = sc.nextLine();

        administratorsDAO.deleteAdministrator(user_id);
        System.out.println("'" + user_id + "' 계정이 정상 삭제 되었습니다.");
    }

    public static void function(String[] args) {
        try{
            AdministratorsUI ui = new AdministratorsUI();
            ui.start();
        }catch (Exception e){
            System.out.println("에러 메세지: " + e.getMessage());
        }
    }
}

