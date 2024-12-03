package org.example.logistics.administratorsWarehouses;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class AdministratorsWarehousesUI {
    private AdministratorsWarehousesDAO administrators_warehousesDAO;

    // 생성자 : DAO(Data Access Object) 초기화
    public AdministratorsWarehousesUI() throws SQLException {
        // 데이터베이스 연동 객체 생성
        this.administrators_warehousesDAO = new AdministratorsWarehousesDAO();
    }

    // 프로그램 시작 및 사용자 인터페이스를 제공하는 메서드
    public void start() {
        // Scanner는 자바에서 콘솔 입력을 받기 위해 주로 사용되는 객체임.
        // Scanner 객체를 사용하여 콘솔에서 사용자 입력을 처리
        Scanner sc = new Scanner(System.in);
        
        while(true) { // 무한 루프로 사용자 입력을 처리
            // 사용자에게 메뉴를 표시
            System.out.println("\n=== 관리자 - 창고 정보 시스템 ===");
            System.out.println("1. 창고 관리자 전체 보기");
            System.out.println("2. 창고 관리자 ID로 조회해서 보기");
            System.out.println("3. 창고 관리자 추가");
            System.out.println("4. 창고 관리자 수정");
            System.out.println("5. 창고 관리자 삭제");
            System.out.println("6. 종료");
            System.out.println("선택: ");

            // 사용자 입력을 점수로 받음
            int select = sc.nextInt();
            sc.nextLine(); // 입력 버퍼 지우기(개행 문자 처리)
            
            try {
                // 사용자 선택에 따라 메서드 호출
                switch (select) {
                    case 1:
                        getAllAdministrators_Warehouses(sc); // 모든 관리자 정보 조회
                        break;
                    case 2:
                        getAdministrators_Warehouse(sc); // 특정 관리자 정보 조회
                        break;
                    case 3:
                        addAdministrators_Warehouse(sc); // 관리자 추가
                        break;
                    case 4:
                        updateAdministrators_Warehouse(sc); // 관리자 정보 수정
                        break;
                    case 5:
                        deleteAdministrators_Warehouse(sc); // 관리자 삭제
                        break;
                    case 6:
                        System.out.println("Exiting..."); // 프로그램 종료
                        return;
                    default:
                        System.out.println("Select a valid option!"); // 유효하지 않은 입력 처리
                }
            } catch (Exception e) {
                // 예외 처리 : 입력 오류나 SQL 예외를 처리
                System.out.println("Error : " + e.getMessage());
            }
        }
        // Error 발생한 코드 :
        //sc.close();
        // 문제 : sc.close()는 Scanner 객체를 닫을 뿐만 아니라 내부적으로 사용하는 System.in 스트림도 닫아버림.
        //       반복문이나 다른 메서드에서 입력을 받으려고 할 때 System.in이 이미 닫혀있기 때문에 입력이 불가능해짐.
        // 해결 : 1. sc.close는 system.in을 닫기 때문에 반복적으로 입력을 받을 필요가 있는 경우 호출하지 않아야 함.
        // ==> System.in은 JVM이 종료될 때 자동으로 닫히기 때문에 명시적으로 sc.close()를 호출하지 않아도 괜찮음.
        // ==> 따라서 sc.close()를 호출하지 않고 while 반복문을 유지해야함.
        //       2. 프로그램 종료 시점에만 close()를 호출하거나 생략하는 방식으로 문제를 해결할 수 있음.
        // ==> 입력 처리가 완전히 끝난 후(프로그램 종료 직전) sc.close()를 호출함.
        // ==> 하지만 반복문 안에서 System.in을 계속 사용할 경우, sc.close()는 생략하는 것이 안전함.
    }

    // 창고 관리자 목록 전체를 조회하여 출력
    private void getAllAdministrators_Warehouses(Scanner sc) throws SQLException {
        // DAO를 통해 데이터베이스에서 모든 관리자 정보를 가져옴
        List<AdministratorsWarehousesVO> administrators_warehouses = administrators_warehousesDAO.getAllAdministrators_Warehouses();
        if (administrators_warehouses.isEmpty()) {
            System.out.println("데이터가 없습니다."); // 결과가 없을 경우 메시지 출력
        } else {
            // 각 관리자 정보를 출력
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

    // 특정 관리자 정보를 ID를 통해 조회하여 출력
    private void getAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("창고 관리자 ID: ");
        int admin_warehouse_id = sc.nextInt(); // 사용자로부터 ID 입력받기

        // DAO를 통해 특정 ID에 대한 정보를 가져옴.
        AdministratorsWarehousesVO administrators_warehouses = AdministratorsWarehousesDAO.getAdministrators_Warehouse(admin_warehouse_id);
        if (administrators_warehouses == null) {
            System.out.println("해당 창고 관리자 ID를 찾을 수 없습니다."); // 결과가 없을 경우 메시지 출력
        } else {
            // 조회한 관리자 정보 출력
            System.out.println("창고 관리자 ID: " + administrators_warehouses.getAdmin_warehouse_id());
            System.out.println("관리자 ID: " + administrators_warehouses.getAdmin_id());
            System.out.println("창고 ID: " + administrators_warehouses.getWarehouse_id());
        }
    }

    // 새로운 창고 관리자를 추가
    private void addAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("관리자 ID 추가: ");
        int adminID = sc.nextInt(); // 관리자 ID 입력
        System.out.println("창고 ID 추가: ");
        int warehouseID = sc.nextInt(); // 창고 ID 입력
// Error 발생한 코드 :
//        System.out.println("할당된 값 추가: ");
//        Timestamp assignedAt = Timestamp.valueOf();
        // ==> Timestamp assignedAt = Timestamp.valueOf();
        // 문제 : Timestamp.valueOf()는 문자열을 매개변수로 받아야함.
        // 매개변수가 없으면 컴파일 오류 발생함.
        // 해결 : 사용자로부터 yyyy-MM-dd HH:mm:ss형식의 입력을 받아 변환함.
        System.out.println("할당된 시간 추가 (yyyy-MM-dd HH:mm:ss): )");
        String newAssignedAtInput = sc.nextLine(); // 날짜 및 시간 입력
        Timestamp newAssignedAt = Timestamp.valueOf(newAssignedAtInput); // 문자열을 Timestamp로 변환

        // VO 객체 생성 및 데이터 설정
        AdministratorsWarehousesVO administrators_warehouse = new AdministratorsWarehousesVO();
        administrators_warehouse.setAdmin_warehouse_id(Integer.parseInt(sc.nextLine()));
        administrators_warehouse.setAdmin_id(adminID);
        administrators_warehouse.setAdmin_warehouse_id(warehouseID);
        administrators_warehouse.setAssigned_at(newAssignedAt);

        // DAO를 통해 데이터베이스에 추가
        administrators_warehousesDAO.addAdministrators_Warehouse(administrators_warehouse);
        System.out.println("창고 관리자가 성공적으로 추가되었습니다.");
    }

    // 창고 관리자 정보를 수정
    private void updateAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("수정할 창고 관리자 ID: ");
        int adminWarehouseID = sc.nextInt();
        sc.nextLine(); // 버퍼 정리

        // DAO를 통해 기존 데이터를 가져옴
        AdministratorsWarehousesVO administrators_warehouse = administrators_warehousesDAO.getAdministrators_Warehouse(adminWarehouseID);
        if (administrators_warehouse.getAssigned_at() != null) {
            // 기존 데이터를 보여주고 새 데이터를 입력받음
            System.out.println("현재 창고 관리자 ID: " + administrators_warehouse.getAdmin_warehouse_id());
            System.out.println("수정할 창고 관리자 ID: ");
            int newAdminWarehouseID = sc.nextInt();

            System.out.println("현재 관리자 ID: " + administrators_warehouse.getAdmin_id());
            System.out.println("수정할 관리자 ID: ");
            int newAdminID = sc.nextInt();

            System.out.println("현재 창고 ID: " + administrators_warehouse.getWarehouse_id());
            System.out.println("수정할 창고 ID: ");
            int newWarehouseID = sc.nextInt();

// Error 발생한 코드 :
//            System.out.println("현재 할당된 값: " + administrators_warehouse.getAssigned_at());
//            System.out.println("수정할 할당된 값: ");
//            Timestamp newAssignedAt = sc.nextTimestamp();
            // sc.nextTimestamp() 사용
            // 문제 : Scanner에는 nextTimestamp() 메서드가 없음.
            // 해결 : sc.nextLine()으로 입력받은 뒤 Timestamp.valueOf()를 사용해 변환함.
            System.out.print("새로운 할당 시간 (형식: yyyy-MM-dd HH:mm:ss): ");
            String newAssignedAtInput = sc.nextLine();
            Timestamp newAssignedAt = Timestamp.valueOf(newAssignedAtInput);

            // VO 객체 업데이트
            administrators_warehouse.setAdmin_warehouse_id(newAdminWarehouseID);
            administrators_warehouse.setAdmin_id(newAdminID);
            administrators_warehouse.setWarehouse_id(newWarehouseID);
            administrators_warehouse.setAssigned_at(newAssignedAt);

            // DAO를 통해 데이터베이스 업데이트
            administrators_warehousesDAO.updateAdministrators_Warehouse(administrators_warehouse);
            System.out.println("창고 관리자 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("창고 관리자 정보를 찾을 수가 없습니다.");
        }
    }

    // 창고 관리자 정보를 삭제
    private void deleteAdministrators_Warehouse(Scanner sc) throws SQLException {
        System.out.println("삭제할 창고 관리자 ID를 입력하세요.");
        int adminWarehouseID = sc.nextInt();

        // DAO를 통해 데이터베이스에서 삭제
        administrators_warehousesDAO.deleteAdministrators_Warehouse(adminWarehouseID);
        System.out.println("창고 관리자 ID가 성공적으로 삭제되었습니다.");
    }

    // 프로그램 진입점
    public static void main(String[] args) {
        try {
            // UI 클래스 생성 및 시작
            AdministratorsWarehousesUI ui = new AdministratorsWarehousesUI();
            ui.start();
        } catch (Exception e) {
            // 프로그램 전반의 예외 처리
            e.printStackTrace();
        }
    }
}
