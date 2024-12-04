package org.example.logistics.warehouses;

import org.example.logistics.administratorsWarehouses.AdministratorsWarehousesUI;
import org.example.logistics.warehouseInventory.WarehouseInventoryUI;
import org.example.logistics.warehouseStatistics.warehouseStatistics.WarehouseStatisticsUI;

import java.util.List;
import java.util.Scanner;

public class WarehousesUI {
    public static void start() throws Exception {
        WarehousesDAOInterface dao = new WarehousesDAO();
        WarehouseStatisticsUI ui = new WarehouseStatisticsUI();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("===== 창고 관리입니다. =====");
            System.out.println("| 1. 창고 생성 | 2. 창고 목록 | 3. 창고 수정 | 4. 창고 삭제 | 5. 창고 재고 관리 | 6. 창고 통계 | 7. 창고 관리자 정보 | 0. 종료 |");
            System.out.print("메뉴를 선택하세요: ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    System.out.print("창고 이름: ");
                    String warehouse_name = sc.nextLine();
                    System.out.print("창고 위치: ");
                    String warehouse_loc = sc.nextLine();
                    System.out.print("창고 용량: ");
                    int warehouse_cap = sc.nextInt();
                    dao.addWarehouses(warehouse_name, warehouse_loc, warehouse_cap);
                    System.out.println("창고가 생성되었습니다.");
                    break;
                case 2:
                    List<WarehousesVO> list = dao.getAllWarehouses();
                    if (!list.isEmpty()) {
                        System.out.println("창고 목록:");
                        for (WarehousesVO w : list) {
                            System.out.println(w.getWarehouseId() + " " + w.getName() + " " + w.getLocation() + " " + w.getCapacity());
                        }
                    } else {
                        System.out.println("창고가 없습니다.");
                    }
                    break;
                case 3:
                    System.out.print("수정할 창고 ID: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("바꿀 이름: ");
                    String updateName = sc.nextLine();
                    dao.updateWarehouses(updateName, updateId);
                    break;
                case 4:
                    System.out.print("삭제할 창고 ID: ");
                    int deleteId = sc.nextInt();
                    dao.deleteWarehouses(deleteId);
                    break;
                case 5:
                    WarehouseInventoryUI.function();
                    break;
                case 6:
                    ui.start();
                    break;
                case 7:
                    AdministratorsWarehousesUI.function();
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
            }
        }
    }
    public static void function() {
        try {
            WarehousesUI ui = new WarehousesUI();
            ui.start();
        } catch (Exception e) {
            System.out.println("실패했습니다: " + e.getMessage());
        }
    }
}