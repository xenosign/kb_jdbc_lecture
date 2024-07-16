package org.example.user;

import org.example.common.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageUser {
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

            System.out.println("회원 추가 성공!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
            // 간결화 안한 버전
            // users.forEach(user -> System.out.println(user));
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

    public void logoutUser() {
        System.out.println("User logged out.");
    }

    public void searchUsersByName(String namePart) {
        // 기능을 구현해 주세요
        
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

                if (users.size() > 0) {
                    users.forEach(System.out::println);
                } else {
                    System.out.println("해당 철자가 포함 된 회원이 존재하지 않습니다.");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserById(int id) {
        Connection conn = JDBCUtil.getConnection();
        String sql = "DELETE FROM user_table WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ID 가 " + id + " 인 회원 정보가 성공적으로 제거 되었습니다.");
            } else {
                System.out.println("ID 가 " + id + " 인 회원이 존재하지 않습니다!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
                System.out.println("ID 가 " + id + " 인 회원 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("ID 가 " + id + " 인 회원이 존재하지 않습니다!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
