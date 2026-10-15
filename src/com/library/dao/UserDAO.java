package com.library.dao;

import com.library.DatabaseConnection;
import com.library.PasswordUtil;
import com.library.model.User;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean registerUser(
            String name,
            String email,
            String password,
            String role,
            String course
    ) throws SQLException, GeneralSecurityException {

        String passwordHash = PasswordUtil.hashPassword(password);

        String sql = "INSERT INTO users "
                + "(name, email, password, role, course) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, role);
            statement.setString(5, course);

            return statement.executeUpdate() == 1;
        }
    }

    public User findUserByEmail(String email) throws SQLException {

        String sql = "SELECT user_id, name, email, role "
                + "FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("user_id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("role")
                    );
                }
            }
        }

        return null;
    }

    public boolean verifyLogin(
            String email,
            String password
    ) throws SQLException, GeneralSecurityException {

        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String storedHash = result.getString("password");

                    return PasswordUtil.verifyPassword(
                            password,
                            storedHash
                    );
                }
            }
        }

        return false;
    }
}