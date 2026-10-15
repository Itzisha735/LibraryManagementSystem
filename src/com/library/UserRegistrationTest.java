
package com.library;

import com.library.dao.UserDAO;

import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Scanner;

public class UserRegistrationTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter student email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter a temporary test password: ");
        String password = scanner.nextLine();

        System.out.print("Enter student course: ");
        String course = scanner.nextLine().trim();

        UserDAO userDAO = new UserDAO();

        try {
            boolean registered = userDAO.registerUser(
                    name,
                    email,
                    password,
                    "STUDENT",
                    course
            );

            if (registered) {
                System.out.println("Student registered successfully!");
            } else {
                System.out.println("Registration was not completed.");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Database error. The email may already exist, "
                    + "or the database connection may have failed."
            );
            System.out.println("Details: " + e.getMessage());

        } catch (GeneralSecurityException e) {
            System.out.println("Password hashing failed.");
        }

        scanner.close();
    }
}
