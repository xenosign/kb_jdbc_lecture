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
        // 쿼리가 실패하여 while 문이 실행이 안되면 함수가 31번째 줄에서 종료가 안되고 여기까지 오게 되므로
        // 해당 쿼리에 부합하는 DB 데이터가 없는 것이므로 0 을 리턴
        return 0;
    }

    // 로그인한 사용자가 작성한 todo 를 검색
    @Override
    public void getTodosByUserId(String user_id) {
        // todo DB 중에서 특정 작성자가 작성한 todo 전부를 가져오는 쿼리
        String sql = "SELECT * FROM todo WHERE user_id=?";

        // 로그인 한 사용자에 따라 쿼리가 변경되어야 하므로 PreparedStatement 사용
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 로그인한 사용자의 정보를 이용하여 PreparedStatement 완성
            pstmt.setString(1, user_id);
            // 여러개의 Todo 항목을 받아올 예정이므로, 데이터 추가가 쉬운 ArrayList 컬렉션 사용
            ArrayList<TodoVo> todos = new ArrayList<>();
            // ResultSet 데이터를 이용하여 쿼리의 결과를 저장
            try (ResultSet rs = pstmt.executeQuery()) {
                // 받아온 데이터 전부를 순회하는 while 문
                while (rs.next()) {
                    // 받아온 TodoList 데이터는 id, user_id, todo, is_completed, created_at 정보를 가지므로
                    // 각각의 컬럼명에서 데이터를 가져와서 변수에 저장
                    int id = rs.getInt("id");
                    String userId = rs.getString("user_id");
                    String todo = rs.getString("todo");
                    boolean is_completed = rs.getBoolean("is_completed");
                    Timestamp created_at = rs.getTimestamp("created_at");

                    // Todo 데이터를 관리하는 TodoVo 객체로 만들기 위해 생성자를 사용해서 객체 생성
                    TodoVo todoData = new TodoVo(id, userId, todo, is_completed, created_at);
                    // ArrayList 에 DB 에서 받아온 Todo 데이터 객체를 추가
                    todos.add(todoData);
                }
                System.out.println("===== " + user_id + " 님의 Todo 전체 목록 =====");
                // ArrayList 의 길이가 0 이면 작성한 Todo 가 없다는 것이므로 ArrayList 의 크기를 통해 출력 여부 결정
                if (todos.size() > 0 ) {
                    // 롬복이 오버라이딩한 toString 메서드를 실행하여 데이터 출력
                    todos.forEach((todo) -> System.out.println(todo));
                } else {
                    System.out.println("## 작성하신 Todo 목록이 없습니다!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 위와 같이 특정 사용자가 작성하였으나 완료 여부가 True 것만 검색하는 메서드
    @Override
    public void getCompletedTodosByUserId(String user_id) {
        // 쿼리문의 검색 조건에 is_completed 가 true 인 것만 추가되고 나머지는 전부 동일 
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

    // 위와 같이 특정 사용자가 작성하였으나 완료 여부가 False 인 것만 검색하는 메서드
    @Override
    public void getUncompletedTodosUserId(String user_id) {
        // 쿼리문의 검색 조건에 is_completed 가 false 인 것만 추가되고 나머지는 전부 동일
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

    // 특정 사용자가 작성한 Todo 의 완료 여부를 변경하는 메서드
    @Override
    public void makeTodoCompleted(int id, String user_id) {
        // SET 명령어로 is_completed 컬럼의 값을 True 로 변경하는 쿼리
        // TodoList 는 PrimaryKey 역할을 하는 id 를 조건으로 검색
        // 단, 자기가 작성한 Todo 만 수정이 가능해야 하므로 user_id 검색 조건도 같이 걸어준다
        String sql = "UPDATE todo SET is_completed = TRUE WHERE id = ? AND user_id = ?";

        // PreparedStatement 에 값을 채워서 쿼리를 완성
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, user_id);

            // 해당 쿼리는 1개의 todo 리스트의 완료 여부를 변경하는 것이므로 해당 쿼리가 정상적으로 수행되면
            // 1개의 데이터가 변경되어야 한다 -> 따라서, pstmt.executeUpdate() 의 리턴 값은 1이 나와야 하는 상태
            int affectedRows = pstmt.executeUpdate();
            // 따라서 리턴 된, affectedRows 값이 0 이면 쿼리가 실패한 케이스 0 이상이면 성공한 케이스로 간주하고
            // if 문을 사용하여 상황에 맞는 결과를 출력
            if (affectedRows > 0) {
                System.out.println("## ID 가 " + id + " 인 Todo 가 완료 처리 되었습니다 ##");
            } else {
                System.out.println("## ID 가 " + id + " 인 Todo 는 회원님의 Todo 가 아닙니다 ##");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 새로운 Todo 를 작성하는 메서드
    @Override
    public void createTodo(String todo, String user_id) {
        // 생성 쿼리를 써야 하므로 todo 테이블에 데이터를 삽입하는 INSERT INTO 쿼리 사용
        // 테이블 생성시 걸어 둔 제약 조건으로 인하여, id / is_completed, created_at 은 자동 생성이 되므로
        // 작성자 정보(user_id) 와 할 일(todo) 만 데이터를 넘겨 주면 된다
        String sql = "INSERT INTO todo (user_id, todo) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 사용자 정보와, 할 일을 전달하여 쿼리 수행
            pstmt.setString(1, user_id);
            pstmt.setString(2, todo);
            int affectedRows = pstmt.executeUpdate();

            // 하나의 데이터를 만드는 쿼리를 수행했으므로 쿼리가 성공하면 반영된 데이터의 개수가 1이므로
            // 리턴은 1, 실패하면 0 이 들어오는 상황이므로 상황에 맞는 결과문 출력
            if (affectedRows > 0) {
                System.out.println("## Todo 생성 성공! ##");
            } else {
                System.out.println("## Todo 생성 실패! ##");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 작성 된 Todo 를 삭제하는 메서드
    @Override
    public void deleteTodo(int id, String user_id) {
        // 수정과 마찬가지로 id 를 이용해서 삭제를 시도, 단 자신이 작성한 todo 만 지워야 하므로
        // user_id 조건을 통해 자신이 작성한 todo 인지를 확인한다
        String sql = "DELETE FROM todo WHERE id = ? AND user_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // todo 의 id 와 user_id 를 전달하여 쿼리문을 완성
            pstmt.setInt(1, id);
            pstmt.setString(2, user_id);

            // 하나의 데이터를 삭제하는 쿼리를 수행했으므로 쿼리가 성공하면 삭제된 데이터의 개수가 1이므로
            // 리턴은 1, 실패하면 0 이 들어오는 상황이므로 상황에 맞는 결과문 출력
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

    // todo 와 todo_user 테이블을 합쳐서(JOIN) 하여 새로운 데이터를 출력하는 메서드
    @Override
    public void getAllTodosWithUserName() {
        // todo 테이블과 todo_user 를 서로의 참조키(= 외래키, FOREIGN KEY) 컬럼인 user_id 를 기준으로 합치는 쿼리
        // user_id 를 통해 특정 todo 를 누가 작성했는지 알 수 있으며 두 테이블의 user_id 공유 되므로 해당 키를 기준으로 합쳐야 한다
        // todo 테이블에는 존재하지 않는 todo_user 의 이름(name 컬럼)을 가져오기 위함
        String sql = "SELECT t.id, t.user_id, u.name, t.todo, t.is_completed, t.created_at " + // 원하는 컬럼의 데이터만 가져오도록 설정
                "FROM todo t " + // 우리는 todo 정보를 보고 싶으므로 기준 테이블은 todo 로 설정, 그리고 todo 를 t 라는 약어로 부르겠다고 선언
                "JOIN todo_user u ON t.user_id = u.user_id " + // todo_user 의 user_id 와 todo 의 user_id 가 같은 것을 합치는 조건
                "ORDER BY t.id ASC"; // 보기 편하게 id 를 기준으로 오름차순 정렬하여 테이블 생성

        // 해당 쿼리는 값이 변하지 않으므로 PreparedStatement 가 아닌 고정 쿼리인 Statement 를 사용
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // 쿼리 실행 결과 데이터를 ResultSet 에 저장            
            // ResultSet 를 전부 순회하는 while 문
            while (rs.next()) {
                // 두 테이블이 합쳐진 테이블에서 원하는 정보를 각각의 변수에 저장, 이전 todo 와는 달리 name 컬럼의 정보를 가져올 수 있다
                int id = rs.getInt("id");
                String userId = rs.getString("user_id");
                String userName = rs.getString("name");
                String todo = rs.getString("todo");
                boolean isCompleted = rs.getBoolean("is_completed");
                String createdAt = rs.getTimestamp("created_at").toString();

                // TodoVo 는 name 멤버 변수가 없기 때문에 toString 으로는 이름 정보까지 출력이 불가능한 상태
                // 다라서 이렇게 별도로 원하는 정보를 출력 해줘야 한다
                System.out.printf("id: %d, user_id: %s, 작성자 이름: %s, todo: %s, is_completed: %b, created_at: %s%n",
                        id, userId, userName, todo, isCompleted, createdAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
