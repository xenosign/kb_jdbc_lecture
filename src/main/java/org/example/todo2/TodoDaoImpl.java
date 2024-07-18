package org.example.todo2;

import org.example.common.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDaoImpl implements TodoDao {
    // 접속 정보를 TodoDaoImpl 자체가 가지고 있도록 한 코드
    private final Connection conn;
    // 해당 클래스를 생성하고 사용할 때 DB에 접속이 되면 되므로, 생성 시점에 JDBCUtil 로 부터 접속 정보를 받아서
    // 스스로의 멤버 변수에 보관한다
    public TodoDaoImpl() {
        this.conn = JDBCUtil.getConnection();
    }

    // 특정 사용자가 작성한 Todo 의 개수를 구하는 메서드
    @Override
    public int getTotalCount(String user_id) {
        // SELECT COUNT(*) 쿼리를 이용하여 해당 쿼리문의 수행 결과가 몇개의 데이터를 가져오는지 구하는 쿼리
        String sql = "SELECT COUNT(*) FROM todo WHERE user_id=?";
        // user_id 에 따라 쿼리가 변경 되므로 PreparedStatement 사용
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            // 완성 된 PreparedStatement 을 수행!
            try (ResultSet rs = pstmt.executeQuery()) {
                // while 구문이 실행 되었다는 것은 쿼리가 정상적으로 작동하였다는 것이므로 데이터 개수를 구할 수 있다 
                while (rs.next()) {                   
                    // 데이터의 개수는 COUNT(*)에 의해 첫번째 컬럼에 만들어 졌으므로, 첫번째 컬럼의 값을 가져오기                    
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 쿼리가 실패하여 while 문이 실행이 안되면 여기 까지 오게 되므로 데이터가 없다는 의미의 0 을 리턴하다
        return 0;
    }

    @Override
    public void getTodosByUserId(String user_id) {
        String sql = "SELECT * FROM todo WHERE user_id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            ArrayList<TodoVo> todos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String userId = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    TodoVo todoData = new TodoVo(id, userId, todo, is_completed, created_at);
                    todos.add(todoData);
                }
                System.out.println("===== " + user_id + " 님의 Todo 전체 목록 =====");
                if (todos.size() > 0 ) {
                    todos.forEach((todo) -> System.out.println(todo));
                } else {
                    System.out.println("## 작성하신 Todo 목록이 없습니다!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCompletedTodosByUserId(String user_id) {
        String sql = "SELECT * FROM todo WHERE user_id=? AND is_completed=1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            ArrayList<TodoVo> todos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String userId = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    TodoVo todoData = new TodoVo(id, userId, todo, is_completed, created_at);
                    todos.add(todoData);
                }
                System.out.println("===== " + user_id + " 님의 Todo 전체 목록 =====");
                if (todos.size() > 0 ) {
                    todos.forEach((todo) -> System.out.println(todo));
                } else {
                    System.out.println("## 작성하신 Todo 목록이 없습니다!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUncompletedTodosUserId(String user_id) {
        String sql = "SELECT * FROM todo WHERE user_id=? AND is_completed=0";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            List<TodoVo> uncompletedTodos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String userId = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    TodoVo todoData = new TodoVo(id, userId, todo, is_completed, created_at);

                    uncompletedTodos.add(todoData);
                }
                System.out.println("===== " + user_id + "님의 Todo 미완료 목록 =====");
                if (uncompletedTodos.size() > 0) {
                    uncompletedTodos.forEach((todo) -> System.out.println(todo));
                } else {
                    System.out.println("## Todo 미완료 목록이 없습니다 ##");
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void makeTodoCompleted(int id, String user_id) {
        String sql = "UPDATE todo SET is_completed = TRUE WHERE id = ? AND user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, user_id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("## ID 가 " + id + " 인 Todo 가 완료 처리 되었습니다 ##");
            } else {
                System.out.println("## ID 가 " + id + " 인 Todo 는 회원님의 Todo 가 아닙니다 ##");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTodo(String todo, String user_id) {
        String sql = "INSERT INTO todo (user_id, todo) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user_id);
            pstmt.setString(2, todo);
            pstmt.executeUpdate();

            System.out.println("## Todo 추가 성공! ##");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTodo(int id, String user_id) {
        String sql = "DELETE FROM todo WHERE id = ? AND user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, user_id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("## ID 가 " + id + " 인 Todo 가 삭제처리 되었습니다 ##");
            } else {
                System.out.println("## ID 가 " + id + " 인 Todo 는 회원님의 Todo 가 아닙니다 ##");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllTodosWithUserName() {
        String sql = "SELECT t.id, t.user_id, u.name, t.todo, t.is_completed, t.created_at " +
                "FROM todo t " +
                "JOIN todo_user u ON t.user_id = u.user_id " +
                "ORDER BY t.id ASC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String userId = rs.getString("user_id");
                String userName = rs.getString("name");
                String todo = rs.getString("todo");
                boolean isCompleted = rs.getBoolean("is_completed");
                String createdAt = rs.getTimestamp("created_at").toString();

                System.out.printf("id: %d, user_id: %s, 작성자 이름: %s, todo: %s, is_completed: %b, created_at: %s%n",
                        id, userId, userName, todo, isCompleted, createdAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
