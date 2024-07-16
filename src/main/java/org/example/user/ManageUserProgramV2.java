package org.example.user;

import org.example.common.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ManageUserProgramV2 {
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

        // 입력 받은 정보로 새로운 회원 객체 생성
        User newUser = new User(0, newId, name, newPassword, age, membership, null);

        // 데이터 베이스 접속
        Connection conn = JDBCUtil.getConnection();


        // 매번 새로운 회원 데이터를 추가해야 하므로 변경되는 쿼리를 편리하게 작성하기 위한 PreparedStatement
        String sql = "INSERT INTO user_table (userid, name, password, age, membership) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUser.getUserid());
            pstmt.setString(2, newUser.getName());
            pstmt.setString(3, newUser.getPassword());
            pstmt.setInt(4, newUser.getAge());
            pstmt.setBoolean(5, newUser.isMembership());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("회원이 성공적으로 추가되었습니다.");

    }
}
