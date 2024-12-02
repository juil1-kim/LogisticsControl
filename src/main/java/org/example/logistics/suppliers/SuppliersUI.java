package org.example.logistics.suppliers;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SuppliersUI {
    private SuppliersDAO suppliersDAO;

    public SuppliersUI() throws SQLException {
        this.suppliersDAO = new SuppliersDAO();
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n=== 공급자 관리 시스템 ===");
            System.out.println("1. 공급자 전체 보기");
            System.out.println("2. 공급자 추가");
            System.out.println("3. 공급자 수정");
            System.out.println("4. 공급자 삭제");
            System.out.println("5. 종료");
            System.out.println("선택: ");

            int select = sc.nextInt();
            sc.nextLine();

            try {
                switch(select) {
                    case 1:
                        getAllSuppliers(sc);
                        break;
                    case 2:
                        addSupplier(sc);
                        break;
                    case 3:
                        updateSupplier(sc);
                        break;
                    case 4:
                        deleteSupplier(sc);
                        break;
                    case 5:
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

    // 공급자 목록 보기
    private void getAllSuppliers(Scanner sc) throws SQLException {
        List<SuppliersVO> suppliers = suppliersDAO.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            System.out.printf("\n=== 공급자 관리 시스템 ===");
            for(SuppliersVO supplier : suppliers) {
                System.out.println("ID: " + supplier.getSupplierId() +
                        ", Name: " + supplier.getName() +
                        ", Contact: " + supplier.getContact() +
                        ", Location: " + supplier.getLocation());
            }
        }
    }

    // 공급자 추가
    private void addSupplier(Scanner sc) throws SQLException {
        System.out.println("공급자 이름 추가: ");
        String name = sc.nextLine();
        System.out.println("공급자 전화번호 추가: ");
        String contact = sc.nextLine();
        System.out.println("공급자 위치: ");
        String location = sc.nextLine();

        SuppliersVO supplier = new SuppliersVO();
        supplier.setSupplierId(Integer.parseInt(sc.nextLine()));
        supplier.setName(name);
        supplier.setContact(contact);
        supplier.setLocation(location);

        suppliersDAO.addSupplier(supplier);
        System.out.println("공급자가 성공적으로 추가되었습니다.");
    }

    // 공급자 수정
    private void updateSupplier(Scanner sc) throws SQLException {
        System.out.println("수정할 공급자 아이디: ");
        int id = sc.nextInt();
        sc.nextLine();

        SuppliersVO supplier = suppliersDAO.getSuppliersById(id);
        if (supplier != null) {
            System.out.println("현재 공급자 이름: " + supplier.getName());
            System.out.println("수정할 공급자 이름: ");
            String newName = sc.nextLine();

            System.out.println("현재 공급자 전화번호: " + supplier.getContact());
            System.out.println("수정할 공급자 전화번호: ");
            String newContact = sc.nextLine();

            System.out.println("현재 공급자 위치: " + supplier.getLocation());
            System.out.println("수정할 공급자 위치: ");
            String newLocation = sc.nextLine();

            supplier.setContact(newName);
            supplier.setContact(newContact);
            supplier.setContact(newLocation);

            suppliersDAO.updateSupplier(supplier);
            System.out.println("공급자 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("공급자 정보를 찾을 수가 없습니다.");
        }
    }

    // 공급자 삭제
    private void deleteSupplier(Scanner sc) throws SQLException {
        System.out.println("삭제할 공급자 ID를 입력하세요.");
        int id = sc.nextInt();

        suppliersDAO.deleteSupplier(id);
        System.out.println("공급자 ID가 성공적으로 삭제되었습니다.");
    }

    public static void main(String[] args) {
        try {
            SuppliersUI ui = new SuppliersUI();
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
