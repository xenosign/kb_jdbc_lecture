package org.example.user2;

import java.util.Scanner;

public class ManageUserProgramV2 {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        Scanner scanner = new Scanner(System.in);

        UserVO loggedInUser = null;

        while (loggedInUser == null) {
            System.out.println("회원 관리 프로그램에 오신 것을 환영합니다.");
            System.out.println("로그인을 해주십시오.");
            System.out.print("ID: ");
            String id = scanner.nextLine();
            System.out.print("PASSWORD: ");
            String password = scanner.nextLine();

            loggedInUser = userDAO.loginUser(id, password);
            if (loggedInUser == null) {
                System.out.println("로그인 정보가 잘못되었습니다. 다시 시도하십시오.");
            }
        }

        System.out.println("====== 회원 관리 프로그램 ======");
        while (true) {
            System.out.println("1. 회원 목록 조회");
            System.out.println("2. 회원 추가");
            System.out.println("3. 특정 이름이 포함된 유저 검색");
            System.out.println("4. 회원 삭제");
            System.out.println("5. 회원 정보 수정");
            System.out.println("6. 로그아웃");
            System.out.print("원하는 작업 번호를 입력하세요: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                for (UserVO user : userDAO.getAllUsers()) {
                    System.out.println(user);
                }
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

                UserVO newUser = new UserVO(0, newId, name, newPassword, age, membership, null);
                userDAO.addUser(newUser);
                System.out.println("회원이 성공적으로 추가되었습니다.");
            } else if (choice == 3) {
                System.out.print("검색할 이름의 일부를 입력하세요: ");
                String namePart = scanner.nextLine();
                for (UserVO user : userDAO.searchUsersByName(namePart)) {
                    System.out.println(user);
                }
            } else if (choice == 4) {
                System.out.print("삭제할 회원의 ID를 입력하세요: ");
                int deleteId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                userDAO.deleteUserById(deleteId);
            } else if (choice == 5) {
                System.out.print("수정할 회원의 ID를 입력하세요: ");
                int updateId = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("새 이름: ");
                String newName = scanner.nextLine();
                System.out.print("새 비밀번호: ");
                String newPassword = scanner.nextLine();
                System.out.print("새 나이: ");
                int newAge = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("새 멤버쉽 여부 (true/false): ");
                boolean newMembership = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline

                userDAO.updateUserDetails(updateId, newName, newPassword, newAge, newMembership);
            } else if (choice == 6) {
                userDAO.logoutUser();
                loggedInUser = null;
                System.out.println("로그아웃 되었습니다.");
                break;
            } else {
                System.out.println("잘못된 선택입니다. 다시 시도하십시오.");
            }
        }
        userDAO.close();
    }
}
