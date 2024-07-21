package org.example.user2;

import org.example.common.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection conn;

    public UserDAO() {
        this.conn = JDBCUtil.getConnection();
    }

    // 사용자 추가
    public void addUser(UserVO user) {
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

    // 모든 사용자 조회
    public List<UserVO> getAllUsers() {
        String sql = "SELECT * FROM user_table";
        List<UserVO> users = new ArrayList<>();


        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UserVO user = new UserVO(
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
        return users;
    }

    // 사용자 로그인
    public UserVO loginUser(String userid, String password) {
        String sql = "SELECT * FROM user_table WHERE userid = ? AND password = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userid);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UserVO(
                            rs.getInt("id"),
                            rs.getString("userid"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getInt("age"),
                            rs.getBoolean("membership"),
                            rs.getTimestamp("signup_date")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // 사용자 검색
    public List<UserVO> searchUsersByName(String namePart) {
        String sql = "SELECT * FROM user_table WHERE name LIKE ?";
        List<UserVO> users = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + namePart + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UserVO user = new UserVO(
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    // 사용자 삭제
    public void deleteUserById(int id) {
        String sql = "DELETE FROM user_table WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ID가 " + id + "인 회원 정보가 성공적으로 제거되었습니다.");
            } else {
                System.out.println("ID가 " + id + "인 회원이 존재하지 않습니다!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용자 정보 업데이트
    public void updateUserDetails(int id, String newName, String newPassword, int newAge, boolean newMembership) {
        String sql = "UPDATE user_table SET name = ?, password = ?, age = ?, membership = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, newAge);
            pstmt.setBoolean(4, newMembership);
            pstmt.setInt(5, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ID가 " + id + "인 회원 정보가 성공적으로 업데이트되었습니다.");
            } else {
                System.out.println("ID가 " + id + "인 회원이 존재하지 않습니다!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그아웃 (현재는 단순 메시지 출력용 메서드)
    public void logoutUser() {
        System.out.println("User logged out.");
    }

    // 리소스 해제
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
