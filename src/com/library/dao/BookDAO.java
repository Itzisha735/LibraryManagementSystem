package com.library.dao;

import com.library.DatabaseConnection;
import com.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");

                Book book = new Book(bookId, title, author);

                if (!resultSet.getBoolean("available")) {
                    book.borrowBook();
                }

                books.add(book);
            }

        } catch (SQLException e) {

            System.out.println("Failed to retrieve books.");
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error while loading books.");
            System.out.println("Error: " + e.getMessage());
        }

        return books;
    }

    public void viewAllBooks() {

        List<Book> books = getAllBooks();

        System.out.println("\n===== BOOKS FROM DATABASE =====");

        for (Book book : books) {

            book.displayBookDetails();

            System.out.println("--------------------");
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

    // =========================
    // SEARCH BOOKS FOR WEB
    // =========================

    public List<Book> searchBooksForWeb(String keyword) {

        List<Book> books = new ArrayList<>();

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

                while (resultSet.next()) {

                    int bookId = resultSet.getInt("book_id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");

                    Book book = new Book(bookId, title, author);

                    if (!resultSet.getBoolean("available")) {
                        book.borrowBook();
                    }

                    books.add(book);
                }
            }

        } catch (SQLException e) {

            System.out.println("Failed to search books.");
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {

            System.out.println("Error while searching books.");
            System.out.println("Error: " + e.getMessage());
        }

        return books;
    }

    // =========================
    // BORROW BOOK
    // =========================

    public boolean borrowBook(int bookId) {

        String sql = "UPDATE books "
                   + "SET available = false "
                   + "WHERE book_id = ? AND available = true";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, bookId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Book borrowed successfully.");
                return true;
            }

            System.out.println("Book is already borrowed or does not exist.");
            return false;

        } catch (SQLException e) {

            System.out.println("Failed to borrow book.");
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // RETURN BOOK
    // =========================

    public boolean returnBook(int bookId) {

        String sql = "UPDATE books "
                   + "SET available = true "
                   + "WHERE book_id = ? AND available = false";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, bookId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Book returned successfully.");
                return true;
            }

            System.out.println("Book is already available or does not exist.");
            return false;

        } catch (SQLException e) {

            System.out.println("Failed to return book.");
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // DELETE BOOK
    // =========================

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