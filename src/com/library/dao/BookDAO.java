package com.library.dao;

import com.library.DatabaseConnection;
import com.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {

    public void addBook(Book book) {

        String sql = "INSERT INTO books (book_id, title, author, available) "
                   + "VALUES (?, ?, ?, ?)";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, book.getBookId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setBoolean(4, book.isAvailable());

            statement.executeUpdate();

            System.out.println("Book added to database successfully.");

        } catch (SQLException e) {

            System.out.println("Failed to add book to database.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewAllBooks() {

        String sql = "SELECT * FROM books";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            System.out.println("\n===== BOOKS FROM DATABASE =====");

            while (resultSet.next()) {

                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean available = resultSet.getBoolean("available");

                System.out.println("Book ID: " + bookId);
                System.out.println("Title: " + title);
                System.out.println("Author: " + author);
                System.out.println("Available: " + available);
                System.out.println("--------------------");
            }

        } catch (SQLException e) {

            System.out.println("Failed to retrieve books.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchBooks(String keyword) {

        String sql = "SELECT * FROM books "
                   + "WHERE title LIKE ? OR author LIKE ?";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            String searchKeyword = "%" + keyword + "%";

            statement.setString(1, searchKeyword);
            statement.setString(2, searchKeyword);

            try (ResultSet resultSet = statement.executeQuery()) {

                System.out.println("\n===== DATABASE SEARCH RESULTS =====");

                boolean found = false;

                while (resultSet.next()) {

                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    boolean available = resultSet.getBoolean("available");

                    System.out.println("Book ID: " + bookId);
                    System.out.println("Title: " + title);
                    System.out.println("Author: " + author);
                    System.out.println("Available: " + available);
                    System.out.println("--------------------");

                    found = true;
                }

                if (!found) {
                    System.out.println("No books found.");
                }
            }

        } catch (SQLException e) {

            System.out.println("Failed to search books.");
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void deleteBook(int bookId) {

        String sql = "DELETE FROM books WHERE book_id = ?";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, bookId);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book not found.");
            }

        } catch (SQLException e) {

            System.out.println("Failed to delete book.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}