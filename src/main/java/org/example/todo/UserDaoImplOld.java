package org.example.todo;

import org.example.common.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplOld implements UserDaoOld {
    private final Connection conn;

    public UserDaoImplOld() {
        this.conn = JDBCUtil.getConnection();
    }

    @Override
    public void createUser(UserVo user) {
        String sql = "INSERT INTO user_table (user_id, name, password) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();

            System.out.println("회원 추가 성공!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserVo loginUser(String userId, String password) {
        String sql = "SELECT * FROM user_table WHERE user_id = ? AND password = ?";
        UserVo loginUser = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    loginUser = new UserVo(rs.getString("user_id"), rs.getString("name"), rs.getString("password"), null);
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loginUser;
    }

    @Override
    public void showAllUsers() {
        String sql = "SELECT * FROM user_table";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<UserVo> users = new ArrayList<>();
            while (rs.next()) {
                UserVo user = new UserVo(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at")
                );
                users.add(user);
            }
            users.forEach(user -> System.out.println(user));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(UserVo updateUser) {
        String sql = "UPDATE user_table SET name = ?, password = ? WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updateUser.getName());
            pstmt.setString(2, updateUser.getPassword());
            pstmt.setString(3, updateUser.getUserId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ID 가 " + updateUser.getUserId() + " 인 회원 정보가 성공적으로 업데이트 되었습니다.");
            } else {
                System.out.println("ID 가 " + updateUser.getUserId() + " 인 회원이 존재하지 않습니다!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String userId) {
        String sql = "DELETE FROM user_table WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("ID 가 " + userId + " 인 회원 정보가 성공적으로 제거 되었습니다.");
            } else {
                System.out.println("ID 가 " + userId + " 인 회원이 존재하지 않습니다!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
