
package com.library;

import java.security.GeneralSecurityException;
import java.util.Scanner;

public class PasswordUtilTest {

    public static void main(String[] args)
            throws GeneralSecurityException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a test password: ");
        String password = scanner.nextLine();

        String hashedPassword =
                PasswordUtil.hashPassword(password);

        System.out.println("Password hashed successfully.");
        System.out.println("Stored value has three parts: "
                + (hashedPassword.split(":").length == 3));

        System.out.println("Correct password accepted: "
                + PasswordUtil.verifyPassword(
                        password, hashedPassword));

        System.out.println("Wrong password rejected: "
                + !PasswordUtil.verifyPassword(
                        "DefinitelyWrong123!", hashedPassword));

        scanner.close();
    }
}
