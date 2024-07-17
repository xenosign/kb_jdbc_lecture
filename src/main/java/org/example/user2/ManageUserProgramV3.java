package org.example.user2;

import java.util.Scanner;

public class ManageUserProgramV3 {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        userDAO.getAllUsers();
        userDAO.close();
    }
}
