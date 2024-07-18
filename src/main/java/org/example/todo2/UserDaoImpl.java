package org.example.todo2;

import org.example.common.JDBCUtil;

import java.sql.*;

public class UserDaoImpl implements UserDao {
    private final Connection conn;

    public UserDaoImpl() {
        this.conn = JDBCUtil.getConnection();
    }

    @Override
    public UserVo loginUser(String user_id, String password) {
        String sql = "SELECT * FROM todo_user WHERE user_id=? AND password=?";
        UserVo loginUser = null;

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            pstmt.setString(2, password);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    String loginUserId = rs.getString("user_id");
                    String loginName = rs.getString("name");
                    String loginPassword = rs.getString("password");
                    Timestamp loginCreatedAt = rs.getTimestamp("created_at");

                    loginUser = new UserVo(loginUserId, loginName, loginPassword, loginCreatedAt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginUser;
    }
}












