package org.example.todo;

import org.example.common.JDBCUtil;
import org.example.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDaoImpl implements TodoDao {
    private final Connection conn;

    public TodoDaoImpl() {
        this.conn = JDBCUtil.getConnection();
    }

    @Override
    public  int getTotalCount(String userId) {
        String sql = "select count(*) from todo where user_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try(ResultSet rs= stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void getTodosByUserId(String userId) {
        String sql = "SELECT * FROM todo WHERE user_id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            List<TodoVo> todos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String user_id = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    TodoVo todoData = new TodoVo(id, user_id, todo, is_completed, created_at);
                    todos.add(todoData);
                }
                System.out.println("===== "+ userId + "님의 Todo 전체 목록 =====");
                if (todos.size() > 0) {
                    todos.forEach((todo) -> System.out.println(todo));
                } else {
                    System.out.println("## Todo 목록이 없습니다 ##");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getCompletedTodosByUserId(String userId) {
        String sql = "SELECT * FROM todo WHERE user_id=? AND is_completed=1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            List<TodoVo> completedTodos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String user_id = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    TodoVo todoData = new TodoVo(id, user_id, todo, is_completed, created_at);
                    completedTodos.add(todoData);
                }
                
                System.out.println("===== "+ userId + "님의 Todo 완료 목록 =====");
                if (completedTodos.size() > 0) {
                    completedTodos.forEach((todo) -> System.out.println(todo));
                } else {
                    System.out.println("## Todo 완료 목록이 없습니다 ##");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getUncompletedTodosUserId(String userId) {
        String sql = "SELECT * FROM todo WHERE user_id=? AND is_completed=0";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            List<TodoVo> uncompletedTodos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String user_id = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    TodoVo todoData = new TodoVo(id, user_id, todo, is_completed, created_at);

                    uncompletedTodos.add(todoData);
                }
                System.out.println("===== "+ userId + "님의 Todo 미완료 목록 =====");
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
    public void makeTodoCompleted(int id, String userId) {
        String sql = "UPDATE todo SET is_completed = TRUE WHERE id = ? AND user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, userId);

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
    public void createTodo(String todo, String userId) {
        String sql = "INSERT INTO todo (user_id, todo) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, todo);
            pstmt.executeUpdate();

            System.out.println("## Todo 추가 성공! ##");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTodo(int id, String userId) {
        String sql = "DELETE FROM todo WHERE id = ? AND user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, userId);

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
}
