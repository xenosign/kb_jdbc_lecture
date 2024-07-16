package org.example.user;

import java.util.Scanner;

public class ManageUserProgramV4 {
    public static void main(String[] args) {
        ManageUser manageUser = new ManageUser();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("====== 회원 관리 프로그램 ======");
            System.out.println("1. 회원 목록 조회");
            System.out.println("2. 회원 추가");
            System.out.println("3. 회원 삭제");
            System.out.println("4. 특정 이름을 가지는 회원 조회");
            System.out.print("원하는 작업 번호를 입력하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                manageUser.getAllUsers();
            } else if (choice == 2) {
                System.out.print("추가할 회원의 ID: ");
                String newId = scanner.nextLine();
                System.out.print("이름: ");
                String name = scanner.nextLine();
                System.out.print("비밀번호: ");
                String newPassword = scanner.nextLine();
                System.out.print("나이: ");
                int age = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("멤버쉽 여부 (true/false): ");
                boolean membership = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline

                User newUser = new User(0, newId, name, newPassword, age, membership, null);
                manageUser.addUser(newUser);
            } else if (choice == 3) {
                System.out.print("삭제할 회원의 ID: ");
                int deleteId = scanner.nextInt();
                manageUser.deleteUserById(deleteId);
            } else if (choice == 4) {
                System.out.print("검색할 이름의 일부를 입력하세요 : ");
                String namePart = scanner.nextLine();
                manageUser.searchUsersByName(namePart);
            } else {
                System.out.println("잘못된 선택입니다. 다시 시도하십시오.");
            }
        }
    }
}
