package org.example.logistics.warehouseInventory;

import org.example.logistics.orders.OrdersVO;

import java.util.ArrayList;
import java.util.Scanner;

public class WarehouseInventoryUI {
    public static void main(String[] args) throws Exception {
        int id = 0;
        int productId = 0;
        int quantity = 0;
        WarehouseInventoryDAO dao = new WarehouseInventoryDAO();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("===== 창고 재고 관리입니다. =====");
            System.out.println("1. 창고 재고 보기 | 2. 창고 재고 요청 | 0. 끝내기");
            System.out.print("메뉴선택: ");
            int menu = sc.nextInt();
            switch (menu) {
                case 1:
                    System.out.print("창고 id: ");
                    id = sc.nextInt();

                    ArrayList<WarehouseInventoryVO> list = dao.getAllInventoryDAO(id);
                    System.out.println("=====재고 목록=====");
                    System.out.println("| ID | 상품 이름   | 가격  | 수량 | 업데이트날짜 |");
                    for (WarehouseInventoryVO bag : list) {
                        System.out.println("| " + bag.getProductId() + "  | " + bag.getProductName() + " | " + bag.getProductPrice() + " | " + bag.getQuantity() + " |" + bag.getLast_update());
                    }
                    break;
                case 2:
                    System.out.print("창고 id: ");
                    id = sc.nextInt();
                    System.out.print("입고할 상품ID를 입력하세요: ");
                    productId = sc.nextInt();
                    System.out.print("수량을 입력하세요: ");
                    quantity = sc.nextInt();
                    dao.addInventoryDAO(id, productId, quantity);
                    break;
            }
            if(menu == 0){
                System.out.println("창고 관리를 종료합니다.");
                break;
            }
        }
    }
}