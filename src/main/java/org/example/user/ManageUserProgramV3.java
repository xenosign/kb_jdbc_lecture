package org.example.user;

import org.example.common.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageUserProgramV3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 회원 정보를 입력 받기
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

        // 이전 코드 내용

        // 입력 받은 정보로 새로운 회원 객체 생성
        User newUser = new User(0, newId, name, newPassword, age, membership, null);

        // DB 통신을 전담하는 인스턴스의 메서드를 사용하여 OOP 적 구현
        ManageUser manageUser = new ManageUser();
        // 생성한 회원 객체를 전달
        manageUser.addUser(newUser);


        // 삭제할 회원의 id 입력 받기
        System.out.print("삭제할 회원의 ID: ");
        int deleteId = scanner.nextInt();

        // 기능을 구현한 deleteUserById 를 사용하여 회원 삭제 진행
        manageUser.deleteUserById(deleteId);

    }
}
