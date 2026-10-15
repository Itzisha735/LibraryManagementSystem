
package com.library;

import com.library.dao.UserDAO;
import com.library.model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class UserDAOTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter email to search: ");
        String email = scanner.nextLine().trim();

        UserDAO userDAO = new UserDAO();

        try {
            User user = userDAO.findUserByEmail(email);

            if (user != null) {
                System.out.println("User found!");
                user.displayUserDetails();
            } else {
                System.out.println("No user found with that email.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: "
                    + e.getMessage());
        }

        scanner.close();
    }
}
