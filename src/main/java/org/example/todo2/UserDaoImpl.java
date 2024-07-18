package org.example.todo2;

import org.example.common.JDBCUtil;

import java.sql.*;

public class UserDaoImpl implements UserDao {
    // 접속 정보를 UserDaoImpl 자체가 가지고 있도록 한 코드
    private final Connection conn;
    // 해당 클래스를 생성하고 사용할 때 DB에 접속이 되면 되므로, 생성 시점에 JDBCUtil 로 부터 접속 정보를 받아서
    // 스스로의 멤버 변수에 보관한다
    public UserDaoImpl() {
        this.conn = JDBCUtil.getConnection();
    }

    // 로그인 기능 구현
    @Override
    public UserVo loginUser(String user_id, String password) {
        // todo_user 테이블에서 매개 변수로 전달 받은 user_id 와 password 가 일치하는 데이터가 있는지 확인하면 되므로
        // SELECT 문의 검색 조건을 WHERE user_id=? AND password=? 이런 방식으로 2개를 걸어서 검색
        String sql = "SELECT * FROM todo_user WHERE user_id=? AND password=?";

        // 해당 메서드는 로그인이 성공하면 로그인 된 사용자의 정보를 객체로 만들어 리턴하므로 미리 UserVo 타입의 객체를 생성한다
        // 지금은 로그인 전이므로 빈 값인 null 을 부여
        UserVo loginUser = null;

        // 로그인 할 때마다 다른 사용자 정보 값이 입력될 것이므로, PreparedStatement 를 사용하여 편리하게 쿼리를 변경하고 성능 최적화
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // PreparedStatement 의 첫번째와 두번째 ? 의 값에 매개 변수로 전달 받은 user_id 와 password 를 전달하여 쿼리문 완성
            pstmt.setString(1, user_id);
            pstmt.setString(2, password);

            // 완성된 쿼리를 수행하고, DB 로 부터 결과를 받아온다
            // 결과는 몇개가 들어올지 모르므로 ResultSet 을 이용하여 받는다
            // ResultSet 참고 블로그 : https://aricode.tistory.com/10
            try(ResultSet rs = pstmt.executeQuery()) {
                // ResultSet 은 다음 데이터가 있는지를 rs.next() 를 통해 확인이 가능하므로 해당 while 구문이 실행 되는 것은
                // 적어도 보낸 쿼리를 만족하는 데이터가 1개 이상 존재한다는 의미이므로 로그인에 성공한 케이스!
                while(rs.next()) {
                    // DB로 부터 받은 정보를 각각의 변수에 담기
                    String loginUserId = rs.getString("user_id");
                    String loginName = rs.getString("name");
                    String loginPassword = rs.getString("password");
                    Timestamp loginCreatedAt = rs.getTimestamp("created_at");

                    // 각각의 변수에 담긴 정보를 이용하여 UserVo 객체를 생성하여 loginUser 에 넣기!
                    // 여기 까지 오면 로그인이 성공한 케이스로 결국 return loginUser 이 수행되므로
                    // 로그인한 사용자 정보가 리턴 된다
                    loginUser = new UserVo(loginUserId, loginName, loginPassword, loginCreatedAt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginUser;
    }
}












