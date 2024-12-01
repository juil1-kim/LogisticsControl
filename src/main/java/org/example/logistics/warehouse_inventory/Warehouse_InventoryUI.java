package org.example.logistics.warehouse_inventory;

import java.util.ArrayList;
import java.util.Scanner;

public class Warehouse_InventoryUI {
    public static void main(String[] args) throws Exception {
        Warehouse_InventoryDAO dao = new Warehouse_InventoryDAO();
        Scanner sc = new Scanner(System.in);
        int productId = 0;
        int id = 0;
        while (true) {
            System.out.println("1. 창고 재고 보기");
            System.out.println("2. 창고 재고 요청");
            System.out.println("0. 끝내기");
            System.out.print("메뉴선택: ");
            int menu = sc.nextInt();
            switch (menu) {
                case 1:
                    System.out.print("창고 id: ");
                    id = sc.nextInt();

                    ArrayList<Warehouse_InventoryVO> list = dao.getAllInventoryDAO(id);
                    System.out.println("=====재고 목록=====");
                    System.out.println("| ID" + " | 상품 이름 " + " | 가격" + " | 수량 |");
                    for (Warehouse_InventoryVO bag : list) {
                        System.out.println("| " + bag.getProductId() + " | " + bag.getProductName() + " | " + bag.getProductPrice() + " | " + bag.getQuantity() + " |");
                    }
                    break;
                case 2:
                    System.out.print("창고 id: ");
                    id = sc.nextInt();
                    System.out.print("입고할 상품ID를 입력하세요: ");
                    break;
            }
            if(menu == 0){
                System.out.println("창고 관리를 종료합니다.");
                break;
            }
        }
    }
}