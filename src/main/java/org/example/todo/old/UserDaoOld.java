package org.example.todo.old;

import org.example.todo.UserVo;

public interface UserDaoOld {
    void createUser(UserVo user);
    UserVo loginUser(String userId, String password);
    void showAllUsers();
    void updateUser(UserVo updateUser);
    void deleteUser(String userId);
}
