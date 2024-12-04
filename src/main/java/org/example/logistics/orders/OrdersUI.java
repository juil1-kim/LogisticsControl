package org.example.logistics.orders;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class OrdersUI {

    int branchId;
    int warehouseId;
    int productId;
    int quantity;
    int orderId;
    Scanner sc = new Scanner(System.in);
    OrdersDAOInterface ordersDao;

    public OrdersUI() throws Exception {
        branchId = 0;
        warehouseId = 0;
        productId = 0;
        quantity = 0;
        orderId = 0;
        ordersDao = new OrdersDAO();
    }

    public void WarehouseOrdersUI() throws Exception {

        DecimalFormat df = new DecimalFormat("###,###");
        Scanner sc = new Scanner(System.in);
        OrdersDAOInterface ordersDao = new OrdersDAO();
        while (true) {
            System.out.println("===== 주문 관리 =====");
            System.out.println("1. 주문 목록 | 2. 주문 수락 | 0. 종료");
            System.out.print("메뉴를 선택하세요: ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    List<OrdersVO> list = ordersDao.getAllOrder();
                    System.out.println("| 주문 | 창고 | 지점 | 상태 | 상품 | 수량 | 총 금액 | 주문날짜 |");
                    System.out.println("==================================================");
                    for (OrdersVO o : list) {
                        System.out.println("| " + o.getOrderId() +  " | " + o.getWarehouseId() +
                                        " | " + o.getBranchId() +   " | " + o.getStatus() +
                                        " | " + o.getProductId() +  " | " + o.getQuantity() +
                                        " | " + df.format(o.getSumPrice()) +  "원 | " + o.getOrderDate());
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.print("수락할 주문 ID (0. 취소): ");
                    orderId = sc.nextInt();
                    if (orderId == 0) {
                        break;
                    }
                    ordersDao.processOrder(orderId);
                    break;
                case 0:
                    return;
            }
        }
    }

    public void BranchOrdersUI() throws Exception {

        while (true) {
            System.out.println("===== 주문 신청 =====");
            System.out.println("1. 주문 신청 | 0. 종료");
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
                    ordersDao.addOrder(warehouseId, branchId, productId, quantity);
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void warehouseFunction(){
        try{
            OrdersUI ui = new OrdersUI();
            ui.WarehouseOrdersUI();
        }catch (Exception e){
            System.out.println("에러 메세지: " + e.getMessage());
        }
    }
    public static void branchFunction(){
        try{
            OrdersUI ui = new OrdersUI();
            ui.BranchOrdersUI();
        }catch (Exception e){
            System.out.println("에러 메세지: " + e.getMessage());
        }
    }
}
