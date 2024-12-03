package org.example.logistics.administratorsWarehouses;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class AdministratorsWarehousesUI {
    private AdministratorsWarehousesDAO administrators_warehousesDAO;

    public AdministratorsWarehousesUI() throws SQLException {
        this.administrators_warehousesDAO = new AdministratorsWarehousesDAO();
    }
    
    public void start() {
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.println("\n=== 관리자 - 창고 정보 시스템 ===");
            System.out.println("1. 창고 관리자 전체 보기");
            System.out.println("2. 창고 관리자 ID로 조회해서 보기");
            System.out.println("3. 창고 관리자 추가");
            System.out.println("4. 창고 관리자 수정");
            System.out.println("5. 창고 관리자 삭제");
            System.out.println("6. 종료");
            System.out.println("선택: ");
            
            int select = sc.nextInt();
            sc.nextLine();
            
            try {
                switch (select) {
                    case 1:
                        getAllAdministrators_Warehouses(sc);
                        break;
                    case 2:
                        getAdministrators_Warehouse(sc);
                        break;
                    case 3:
                        addAdministrators_Warehouse(sc);
                        break;
                    case 4:
                        updateAdministrators_Warehouse(sc);
                        break;
                    case 5:
                        deleteAdministrators_Warehouse(sc);
                        break;
                    case 6:
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

    // 창고 관리자 목록 전체 보기
    private void getAllAdministrators_Warehouses(Scanner sc) throws SQLException {
        List<AdministratorsWarehousesVO> administrators_warehouses = administrators_warehousesDAO.getAllAdministrators_Warehouses();
        if (administrators_warehouses.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            System.out.printf("\n=== 관리자 - 창고 정보 시스템 ===");
            for (AdministratorsWarehousesVO administrators_warehouse : administrators_warehouses) {
                System.out.println("Admin_Warehouse_ID: " + administrators_warehouse.getAdmin_warehouse_id() +
                        ", Admin_ID: " + administrators_warehouse.getAdmin_id() +
                        ", Warehouse_ID: " + administrators_warehouse.getWarehouse_id() +
                        ", Assigned_at: " + administrators_warehouse.getAssigned_at()
                );
            }
        }
    }

    // 특정 창고 관리자 목록 보기
    private void getAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("창고 관리자 ID: ");
        int admin_warehouse_id = sc.nextInt();

        AdministratorsWarehousesVO administrators_warehouses = AdministratorsWarehousesDAO.getAdministrators_Warehouse(admin_warehouse_id);
        if (administrators_warehouses == null) {
            System.out.println("해당 창고 관리자 ID를 찾을 수 없습니다.");
        } else {
            System.out.println("창고 관리자 ID: " + administrators_warehouses.getAdmin_warehouse_id());
            System.out.println("관리자 ID: " + administrators_warehouses.getAdmin_id());
            System.out.println("창고 ID: " + administrators_warehouses.getWarehouse_id());
        }
    }

    // 창고 관리자 추가
    private void addAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("관리자 ID 추가: ");
        int adminID = sc.nextInt();
        System.out.println("창고 ID 추가: ");
        int warehouseID = sc.nextInt();
//        System.out.println("할당된 값 추가: ");
//        Timestamp assignedAt = Timestamp.valueOf();
        // ==> Timestamp assignedAt = Timestamp.valueOf();
        // 문제 : Timestamp.valueOf()는 문자열을 매개변수로 받아야함.
        // 매개변수가 없으면 컴파일 오류 발생함.
        // 해결 : 사용자로부터 yyyy-MM-dd HH:mm:ss형식의 입력을 받아 변환함.
        System.out.println("할당된 시간 추가 (yyyy-MM-dd HH:mm:ss): )");
        String newAssignedAtInput = sc.nextLine();
        Timestamp newAssignedAt = Timestamp.valueOf(newAssignedAtInput);

        AdministratorsWarehousesVO administrators_warehouse = new AdministratorsWarehousesVO();
        administrators_warehouse.setAdmin_warehouse_id(Integer.parseInt(sc.nextLine()));
        administrators_warehouse.setAdmin_id(adminID);
        administrators_warehouse.setAdmin_warehouse_id(warehouseID);
        administrators_warehouse.setAssigned_at(newAssignedAt);

        administrators_warehousesDAO.addAdministrators_Warehouse(administrators_warehouse);
        System.out.println("창고 관리자가 성공적으로 추가되었습니다.");
    }

    // 창고 관리자 수정
    private void updateAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("수정할 창고 관리자 ID: ");
        int adminWarehouseID = sc.nextInt();
        sc.nextLine();

        AdministratorsWarehousesVO administrators_warehouse = administrators_warehousesDAO.getAdministrators_Warehouse(adminWarehouseID);
        if (administrators_warehouse.getAssigned_at() != null) {
            System.out.println("현재 창고 관리자 ID: " + administrators_warehouse.getAdmin_warehouse_id());
            System.out.println("수정할 창고 관리자 ID: ");
            int newAdminWarehouseID = sc.nextInt();

            System.out.println("현재 관리자 ID: " + administrators_warehouse.getAdmin_id());
            System.out.println("수정할 관리자 ID: ");
            int newAdminID = sc.nextInt();

            System.out.println("현재 창고 ID: " + administrators_warehouse.getWarehouse_id());
            System.out.println("수정할 창고 ID: ");
            int newWarehouseID = sc.nextInt();

//            System.out.println("현재 할당된 값: " + administrators_warehouse.getAssigned_at());
//            System.out.println("수정할 할당된 값: ");
//            Timestamp newAssignedAt = sc.nextTimestamp();
            // sc.nextTimestamp() 사용
            // 문제 : Scanner에는 nextTimestamp() 메서드가 없음.
            // 해결 : sc.nextLine()으로 입력받은 뒤 Timestamp.valueOf()를 사용해 변환함.
            System.out.print("새로운 할당 시간 (형식: yyyy-MM-dd HH:mm:ss): ");
            String newAssignedAtInput = sc.nextLine();
            Timestamp newAssignedAt = Timestamp.valueOf(newAssignedAtInput);
            
            administrators_warehouse.setAdmin_warehouse_id(newAdminWarehouseID);
            administrators_warehouse.setAdmin_id(newAdminID);
            administrators_warehouse.setWarehouse_id(newWarehouseID);
            administrators_warehouse.setAssigned_at(newAssignedAt);

            administrators_warehousesDAO.updateAdministrators_Warehouse(administrators_warehouse);
            System.out.println("창고 관리자 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("창고 관리자 정보를 찾을 수가 없습니다.");
        }
    }

    // 창고 관리자 삭제
    private void deleteAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("삭제할 창고 관리자 ID를 입력하세요.");
        int adminWarehouseID = sc.nextInt();

        administrators_warehousesDAO.deleteAdministrators_Warehouse(adminWarehouseID);
        System.out.println("창고 관리자 ID가 성공적으로 삭제되었습니다.");
    }

    public static void main(String[] args) {
        try {
            AdministratorsWarehousesUI ui = new AdministratorsWarehousesUI();
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
