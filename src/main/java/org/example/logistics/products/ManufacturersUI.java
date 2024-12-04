package org.example.logistics.products;

import java.util.List;
import java.util.Scanner;

public class ManufacturersUI {
    private static final ManufacturersDAO dao = new ManufacturersDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("=== 제조사 관리 시스템 ===");
                System.out.println("1. 제조사 목록 보기");
                System.out.println("2. 제조사 추가");
                System.out.println("3. 제조사 수정");
                System.out.println("4. 제조사 삭제");
                System.out.println("0. 이전 메뉴로 돌아가기");
                System.out.print("선택: ");

                int choice = getIntInput(">> ");

                switch (choice) {
                    case 1 -> displayManufacturers();
                    case 2 -> addManufacturer();
                    case 3 -> updateManufacturer();
                    case 4 -> deleteManufacturer();
                    case 0 -> {
                        System.out.println("메뉴로 돌아갑니다.");
                        return;
                    }
                    default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
                }
            }
        } finally {
            scanner.close();
        }
    }

    // 유효한 정수 입력 받기
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력하세요.");
            }
        }
    }

    // 문자열 입력 받기
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // 제조사 목록 보기
    private static void displayManufacturers() {
        List<ManufacturersVO> manufacturers = dao.getAllManufacturers();

        if (manufacturers.isEmpty()) {
            System.out.println("등록된 제조사가 없습니다.");
            return;
        }

        System.out.println("제조사 정보");
        System.out.println("=".repeat(70));
        System.out.printf("%-5s | %-20s | %-20s | %-15s%n", "ID", "제조사 이름", "위치", "연락처");
        System.out.println("-".repeat(70));

        manufacturers.sort((a, b) -> Integer.compare(a.getManufacturerId(), b.getManufacturerId()));
        for (ManufacturersVO manufacturer : manufacturers) {
            System.out.printf(
                    "%-5s | %-20s | %-20s | %-15s%n",
                    manufacturer.getManufacturerId(),
                    padString(manufacturer.getName(), 15),
                    padString(manufacturer.getLocation(), 15),
                    padString(manufacturer.getContact(), 10)
            );
        }

        System.out.println("=".repeat(70));
    }

    // 제조사 추가
    private static void addManufacturer() {
        String name = getStringInput("제조사 이름: ");
        String location = getStringInput("위치: ");
        String contact = getStringInput("연락처: ");

        if (dao.addManufacturer(name, location, contact)) {
            System.out.println("제조사가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("제조사 추가에 실패했습니다.");
        }
    }

    // 제조사 수정
    private static void updateManufacturer() {
        int id = getIntInput("수정할 제조사 ID: ");
        String name = getStringInput("새 제조사 이름: ");
        String location = getStringInput("새 위치: ");
        String contact = getStringInput("새 연락처: ");

        if (dao.updateManufacturer(id, name, location, contact)) {
            System.out.println("제조사 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("제조사 수정에 실패했습니다.");
        }
    }

    // 제조사 삭제
    private static void deleteManufacturer() {
        int id = getIntInput("삭제할 제조사 ID: ");

        try {
            if (dao.deleteManufacturer(id)) {
                System.out.println("제조사가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("제조사 삭제에 실패했습니다.");
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 문자열 길이를 고정하는 유틸리티 메서드 and Null 값 처리
    private static String padString(String input, int length) {
        if (input == null) input = ""; // null 값 처리
        return String.format("%-" + length + "s", input);
    }
}
