
package com.library;

import com.library.dao.UserDAO;

import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Scanner;

public class UserLoginTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter registered email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter test password: ");
        String password = scanner.nextLine();

        UserDAO userDAO = new UserDAO();

        try {
            boolean loginSuccessful =
                    userDAO.verifyLogin(email, password);

            if (loginSuccessful) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid email or password.");
            }

        } catch (SQLException | GeneralSecurityException e) {
            System.out.println("Login test failed: "
                    + e.getMessage());
        }

        scanner.close();
    }
}
