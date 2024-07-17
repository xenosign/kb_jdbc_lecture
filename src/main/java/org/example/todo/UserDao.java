package org.example.todo;

import java.util.List;

public interface UserDao {
    UserVo loginUser(String userId, String password);
}
