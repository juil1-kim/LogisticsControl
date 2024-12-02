package org.example.logistics.administrators_warehouses;

import java.sql.SQLException;
import java.util.Scanner;

public class Administrators_WarehousesUI {
    private Administrators_WarehousesDAO administrators_warehousesDAO;

    public Administrators_WarehousesUI() throws SQLException {
        this.administrators_warehousesDAO = new Administrators_WarehousesDAO();
    }
    
    public void start() {
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.println("\n=== 창고 관리자 관리 시스템 ===");
            System.out.println("1. 창고 관리자 추가");
            System.out.println("2. 창고 관리자 전체 보기");
            System.out.println("3. 창고 관리자 ID로 조회해서 보기");
            System.out.println("4. 창고 관리자 수정");
            System.out.println("5. 창고 관리자 삭제");
            System.out.println("6. 창고 관리자 전체 삭제");
            System.out.println("7. 종료");
            System.out.println("선택: ");
            
            int select = sc.nextInt();
            sc.nextLine();
            
            try {
                switch (select) {
                    case 1:
                        addAdministrators_Warehouse(sc);
                        break;
                    case 2:
                        viewAllAdministrators_Warehouse(sc);
                        break;
                    case 3:
                        viewAdministrators_WarehouseByID(sc);
                        break;
                    case 4:
                        updateAdministrators_Warehouse(sc);
                        break;
                    case 5:
                        deleteAdministrators_Warehouse(sc);
                        break;
                    case 6:
                        deleteAllAdministrators_Warehouse(sc);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Select a valid option!");
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    }

    // 창고 관리자 목록 보기
    // 창고 관리자 추가
    // 창고 관리자 수정
    // 창고 관리자 삭제
    private void addAdministrators_Warehouse(Scanner sc) {
    }

    private void viewAllAdministrators_Warehouse(Scanner sc) {
        
    }

    private void viewAdministrators_WarehouseByID(Scanner sc) {
        
    }

    private void updateAdministrators_Warehouse(Scanner sc) {
        
    }

    private void deleteAdministrators_Warehouse(Scanner sc) {
        
    }

    private void deleteAllAdministrators_Warehouse(Scanner sc) {
        
    }
}
