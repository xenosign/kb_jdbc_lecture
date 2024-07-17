package org.example.todo;

import org.example.common.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    private final Connection conn;

    public UserDaoImpl() {
        this.conn = JDBCUtil.getConnection();
    }

    @Override
    public UserVo loginUser(String userId, String password) {
        String sql = "SELECT * FROM todo_user WHERE user_id = ? AND password = ?";
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
}
