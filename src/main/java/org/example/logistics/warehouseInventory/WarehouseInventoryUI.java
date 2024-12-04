package org.example.logistics.warehouseInventory;

import java.util.ArrayList;
import java.util.Scanner;

public class WarehouseInventoryUI {
    public static void start() throws Exception {
        int warehouseId;
        int supplierId;
        int productId;
        int quantity;
        WarehouseInventoryDAOInterface dao = new WarehouseInventoryDAO();
        WarehouseInventoryVO vo = new WarehouseInventoryVO();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("===== 창고 재고 관리입니다. =====");
            System.out.println("1. 창고 재고 보기 | 2. 창고 재고 요청 | 3. 창고 주문 내역 | 0. 끝내기");
            System.out.print("메뉴선택: ");
            int menu = sc.nextInt();
            switch (menu) {
                case 1:
                    System.out.print("창고 id: ");
                    warehouseId = sc.nextInt();

                    ArrayList<WarehouseInventoryVO> list = dao.getAllInventory(warehouseId);
                    System.out.println("=====재고 목록=====");
                    System.out.println("| ID | 상품 이름 | 가격 | 수량 | 업데이트날짜 |");
                    for (WarehouseInventoryVO bag : list) {
                        System.out.println("| " + bag.getProductId() + "  | " + bag.getProductName() + " | " + bag.getProductPrice() + " | " + bag.getQuantity() + " |" + bag.getLastUpdate());
                    }
                    break;
                case 2:
                    System.out.print("창고ID: ");
                    warehouseId = sc.nextInt();
                    System.out.print("공급처ID: ");
                    supplierId = sc.nextInt();
                    System.out.print("입고할 상품ID를 입력하세요: ");
                    productId = sc.nextInt();
                    System.out.print("수량을 입력하세요: ");
                    quantity = sc.nextInt();
                    dao.addInventory(warehouseId, productId, supplierId, quantity);
                    break;
                case 3:
                    System.out.println("=====창고 주문 내역=====");
                    ArrayList<IncomingVO> orderList = dao.getAllInventoryOrders();
                    for (IncomingVO or : orderList) {
                        System.out.println("| " + or.getIncomingId() + "  | " + or.getWarehouseId() + " | " + or.getProductId() + " | " + or.getQuantity() + " |" + or.getIncomingDate());
                    }
            }
            if(menu == 0){
                System.out.println("창고 관리를 종료합니다.");
                break;
            }
        }
    }
    public static void function() {
        try {
            WarehouseInventoryUI ui = new WarehouseInventoryUI();
            ui.start();
        } catch (Exception e) {
            System.out.println("실패했습니다: " + e.getMessage());
        }
    }
}