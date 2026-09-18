package com.library;

import com.library.exceptions.BookNotAvailableException;
import com.library.interfaces.Borrowable;
import com.library.interfaces.Searchable;
import com.library.model.Admin;
import com.library.model.Book;
import com.library.model.Student;

public class Main {

    public static void main(String[] args) {

        // Creating a Book object
        Book book = new Book(
                101,
                "Java Programming",
                "Herbert Schildt"
        );

        // Creating a Student object
        Student student = new Student(
                1,
                "Rahul",
                "rahul@example.com",
                "Computer Engineering"
        );

        // Creating an Admin object
        Admin admin = new Admin(
                100,
                "Library Admin",
                "admin@university.com",
                "Administrator"
        );

        System.out.println("===== BOOK DETAILS =====");
        book.displayBookDetails();

        System.out.println("\n===== STUDENT DETAILS =====");
        student.displayStudentDetails();

        System.out.println("\n===== ADMIN DETAILS =====");
        admin.displayAdminDetails();

        System.out.println("\n===== BORROWING BOOK =====");

        try {
            book.borrowBook();
            System.out.println("Book borrowed successfully.");
            System.out.println("Book available: " + book.isAvailable());

        } catch (BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n===== TRYING TO BORROW AGAIN =====");

        try {
            book.borrowBook();
            System.out.println("Book borrowed successfully.");

        } catch (BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n===== RETURNING BOOK =====");

        book.returnBook();
        System.out.println("Book returned successfully.");
        System.out.println("Book available: " + book.isAvailable());

        System.out.println("\n===== POLYMORPHISM =====");

        try {
            Borrowable borrowableBook = book;
            borrowableBook.borrowBook();

            System.out.println("Book borrowed using Borrowable interface.");
            System.out.println("Book available: " + book.isAvailable());

        } catch (BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Searchable searchableBook = book;

        if (searchableBook.searchBook("Java")) {
            System.out.println("Book found using Searchable interface.");
        } else {
            System.out.println("Book not found.");
        }
    }
}