package org.example.logistics.warehouses;

import java.util.List;
import java.util.Scanner;

public class WarehousesUI {
    public static void main(String[] args) throws Exception {
        WarehousesDAO dao = new WarehousesDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("창고 관리 UI입니다.");
            System.out.println("1. 창고 생성");
            System.out.println("2. 창고 목록");
            System.out.println("3. 창고 수정");
            System.out.println("4. 창고 삭제");
            System.out.println("0. 종료");
            System.out.print("메뉴를 선택하세요: ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    System.out.print("창고 이름: ");
                    String warehouse_name = sc.nextLine();
                    System.out.print("창고 위치: ");
                    String warehouse_loc = sc.nextLine();
                    System.out.println("창고 용량: ");
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
                    dao.update(updateName, updateId);
                    break;
                case 4:
                    System.out.print("삭제할 창고 ID: ");
                    int deleteId = sc.nextInt();
                    dao.delete(deleteId);
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
            }
        }
    }
}