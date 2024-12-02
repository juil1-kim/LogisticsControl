package org.example.logistics.products;

import java.util.List;
import java.util.Scanner;

public class ManufacturersUI {
    private static final ManufacturersDAO dao = new ManufacturersDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("=== 제조사 관리 시스템 ===");
            System.out.println("1. 제조사 목록 보기");
            System.out.println("2. 제조사 추가");
            System.out.println("3. 제조사 수정");
            System.out.println("4. 제조사 삭제");
            System.out.println("5. 종료");
            System.out.print("선택: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> displayManufacturers();
                case 2 -> addManufacturer();
                case 3 -> updateManufacturer();
                case 4 -> deleteManufacturer();
                case 5 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            }
        }
    }

    // 제조사 목록 보기
    private static void displayManufacturers() {
        List<ManufacturersVO> manufacturers = dao.getAllManufacturers();

        System.out.println("제조사 정보");
        System.out.println("=".repeat(70));
        System.out.printf("%-5s | %-20s | %-20s | %-15s%n", "ID", "제조사 이름", "위치", "연락처");
        System.out.println("-".repeat(70));

        if (manufacturers.isEmpty()) {
            System.out.println("데이터가 없습니다.");
        } else {
            for (ManufacturersVO manufacturer : manufacturers) {
                System.out.printf(
                        "%-5d | %-20s | %-20s | %-15s%n",
                        manufacturer.getManufacturerId(),
                        manufacturer.getName(),
                        manufacturer.getLocation(),
                        manufacturer.getContact()
                );
            }
        }

        System.out.println("=".repeat(70));
    }

    // 제조사 추가
    private static void addManufacturer() {
        System.out.print("제조사 이름: ");
        String name = scanner.nextLine();

        System.out.print("위치: ");
        String location = scanner.nextLine();

        System.out.print("연락처: ");
        String contact = scanner.nextLine();

        if (dao.addManufacturer(name, location, contact)) {
            System.out.println("제조사가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("제조사 추가에 실패했습니다.");
        }
    }

    // 제조사 수정
    private static void updateManufacturer() {
        System.out.print("수정할 제조사 ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("새 제조사 이름: ");
        String name = scanner.nextLine();

        System.out.print("새 위치: ");
        String location = scanner.nextLine();

        System.out.print("새 연락처: ");
        String contact = scanner.nextLine();

        if (dao.updateManufacturer(id, name, location, contact)) {
            System.out.println("제조사 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("제조사 수정에 실패했습니다.");
        }
    }

    // 제조사 삭제
    private static void deleteManufacturer() {
        System.out.print("삭제할 제조사 ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (dao.deleteManufacturer(id)) {
            System.out.println("제조사가 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("제조사 삭제에 실패했습니다.");
        }
    }
}

