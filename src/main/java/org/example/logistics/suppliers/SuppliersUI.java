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
        //Scanner는 자바에서 콘솔 입력을 받기 위해 주로 사용되는 객체임.
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n=== 공급자 관리 시스템 ===");
            System.out.println("1. 공급자 전체 보기");
            System.out.println("2. 공급자 ID로 조회해서 보기");
            System.out.println("3. 공급자 추가");
            System.out.println("4. 공급자 수정");
            System.out.println("5. 공급자 삭제");
            System.out.println("6. 종료");
            System.out.println("선택: ");

            int select = sc.nextInt();
            sc.nextLine();

            try {
                switch(select) {
                    case 1:
                        getAllSuppliers(sc);
                        break;
                    case 2:
                        getSuppliersById(sc);
                        break;
                    case 3:
                        addSupplier(sc);
                        break;
                    case 4:
                        updateSupplier(sc);
                        break;
                    case 5:
                        deleteSupplier(sc);
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

    // 공급자 목록 전체 보기
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

    // 특정 공급자 목록 보기
    private void getSuppliersById(Scanner sc) throws SQLException {
        System.out.println("공급자 ID: ");
        int supplierId = sc.nextInt();

        SuppliersVO suppliers = SuppliersDAO.getSuppliersById(supplierId);
        if (suppliers == null) {
            System.out.println("해당 공급자 ID를 찾을 수 없습니다.");
        } else {
            System.out.println("이름: " + suppliers.getName());
            System.out.println("전화번호: " + suppliers.getContact());
            System.out.println("위치: " + suppliers.getLocation());
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

            supplier.setName(newName);
            supplier.setContact(newContact);
            supplier.setLocation(newLocation);

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
