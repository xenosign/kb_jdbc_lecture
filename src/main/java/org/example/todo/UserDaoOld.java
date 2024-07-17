package org.example.todo;

public interface UserDaoOld {
    void createUser(UserVo user);
    UserVo loginUser(String userId, String password);
    void showAllUsers();
    void updateUser(UserVo updateUser);
    void deleteUser(String userId);
}
