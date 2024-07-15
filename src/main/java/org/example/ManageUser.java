package org.example;

import org.example.common.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageUser {

    // 전체 사용자 조회
    public void getAllUsers() {
        Connection conn = JDBCUtil.getConnection();
        String sql = "SELECT * FROM user_table";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("userid"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getBoolean("membership"),
                        rs.getTimestamp("signup_date")
                );
                users.add(user);
            }
            users.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용자 추가
    public void addUser(User user) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "INSERT INTO user_table (userid, name, password, age, membership) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserid());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getAge());
            pstmt.setBoolean(5, user.isMembership());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인
    public User loginUser(String userid, String password) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "SELECT * FROM user_table WHERE userid = ? AND password = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userid);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("userid"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getInt("age"),
                            rs.getBoolean("membership"),
                            rs.getTimestamp("signup_date")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그아웃
    public void logoutUser() {
        // 로그아웃 로직이 필요할 경우 구현
        System.out.println("User logged out.");
    }

    // 특정 이름이 포함된 유저 검색
    public void searchUsersByName(String namePart) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "SELECT * FROM user_table WHERE name LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + namePart + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("userid"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getInt("age"),
                            rs.getBoolean("membership"),
                            rs.getTimestamp("signup_date")
                    );
                    users.add(user);
                }
                users.forEach(System.out::println);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 회원 ID로 삭제
    public void deleteUserById(int id) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "DELETE FROM user_table WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with ID " + id + " has been deleted successfully.");
            } else {
                System.out.println("User with ID " + id + " does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 회원 정보 수정
    public void updateUserDetails(int id, String newName, String newPassword, int newAge, boolean newMembership) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "UPDATE user_table SET name = ?, password = ?, age = ?, membership = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, newAge);
            pstmt.setBoolean(4, newMembership);
            pstmt.setInt(5, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User with ID " + id + " has been updated successfully.");
            } else {
                System.out.println("User with ID " + id + " does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
