package org.example.logistics.orders;

import java.util.List;
import java.util.Scanner;

public class OrdersUI {
    public static void main(String[] args) throws Exception {
        int branchId = 0;
        int warehouseId = 0;
        int productId = 0;
        int quantity = 0;
        int orderId = 0;
        Scanner sc = new Scanner(System.in);
        OrdersDAO ordersDao = new OrdersDAO();
        while (true) {
            System.out.println("===== 주문 관리입니다. =====");
            System.out.println("1. 주문 신청 | 2. 주문 목록 | 3. 주문 수락 | 0. 종료");
            System.out.print("메뉴를 선택하세요: ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    System.out.println("=====주문 요청====");
                    System.out.print("창고 ID: ");
                    warehouseId = sc.nextInt();
                    System.out.print("지점 ID: ");
                    branchId = sc.nextInt();
                    System.out.print("물품 ID: ");
                    productId = sc.nextInt();
                    System.out.print("요청 수량: ");
                    quantity = sc.nextInt();
                    ordersDao.createOrder(branchId, warehouseId, productId, quantity);
                    break;
                case 2:
                    List<OrdersVO> list = ordersDao.getAllOrder();
                    System.out.println("| 주문 | 창고 | 지점 | 상태 | 상품 | 수량 | 주문날짜 |");
                    System.out.println("=====================================================");
                    for (OrdersVO o : list) {
                        System.out.println("| " + o.getOrderId() + " | " + o.getWarehouseId() +
                                        " | " + o.getBranchId() + " | " + o.getStatus() +
                                        " | " + o.getProductId() + " | " + o.getQuantity() +
                                        " | " + o.getOrderDate() + " |");
                    }
                    break;
                case 3:
                    System.out.println("수락할 주문 ID:");
                    orderId = sc.nextInt();

                    break;
                case 0:
                    return;
            }
        }
    }
}
