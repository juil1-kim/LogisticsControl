package org.example.logistics.branches;

import java.util.List;
import java.util.Scanner;

public class BranchesUI {
    public static void main(String[] args) throws Exception {
        BranchesDAO dao = new BranchesDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("지점 관리 UI입니다.");
            System.out.println("1. 지점 생성");
            System.out.println("2. 지점 목록");
            System.out.println("3. 지점 수정");
            System.out.println("4. 지점 삭제");
            System.out.println("0. 종료");
            System.out.print("메뉴를 선택하세요: ");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1:
                    System.out.print("지점 이름: ");
                    String name = sc.nextLine();
                    System.out.print("지점 위치: ");
                    String loc = sc.nextLine();
                    dao.addBranches(name, loc);
                    System.out.println("지점이 생성되었습니다.");
                    break;
                case 2:
                    List<BranchesVO> list = dao.getAllBranches();
                    if (!list.isEmpty()) {
                        System.out.println("지점 목록:");
                        for (BranchesVO b : list) {
                            System.out.println(b.getBranchID() + " " + b.getBranchName() + " " + b.getBranchLocate());
                        }
                    } else {
                        System.out.println("지점이 없습니다.");
                    }
                    break;
                case 3:
                    System.out.print("수정할 지점 ID: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("바꿀 이름: ");
                    String updateName = sc.nextLine();
                    dao.update(updateName, updateId);
                    break;
                case 4:
                    System.out.print("삭제할 지점 ID: ");
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
