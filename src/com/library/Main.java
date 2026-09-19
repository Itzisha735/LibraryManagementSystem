package com.library;

import com.library.exceptions.BookNotAvailableException;
import com.library.interfaces.Borrowable;
import com.library.interfaces.Searchable;
import com.library.model.Admin;
import com.library.model.Book;
import com.library.model.Student;

public class Main {

    public static void main(String[] args) {

        // Creating Book objects
        Book book1 = new Book(
                101,
                "Java Programming",
                "Herbert Schildt"
        );

        Book book2 = new Book(
                102,
                "Data Structures",
                "Seymour Lipschutz"
        );

        Book book3 = new Book(
                103,
                "Database Management Systems",
                "Raghu Ramakrishnan"
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

        // Displaying book details
        System.out.println("===== BOOK DETAILS =====");
        book1.displayBookDetails();

        // Displaying student details
        System.out.println("\n===== STUDENT DETAILS =====");
        student.displayStudentDetails();

        // Displaying admin details
        System.out.println("\n===== ADMIN DETAILS =====");
        admin.displayAdminDetails();

        // Exception handling
        System.out.println("\n===== BORROWING BOOK =====");

        try {
            book1.borrowBook();
            System.out.println("Book borrowed successfully.");
            System.out.println("Book available: " + book1.isAvailable());

        } catch (BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n===== TRYING TO BORROW AGAIN =====");

        try {
            book1.borrowBook();
            System.out.println("Book borrowed successfully.");

        } catch (BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Returning book
        System.out.println("\n===== RETURNING BOOK =====");

        book1.returnBook();
        System.out.println("Book returned successfully.");
        System.out.println("Book available: " + book1.isAvailable());

        // Polymorphism
        System.out.println("\n===== POLYMORPHISM =====");

        try {
            Borrowable borrowableBook = book1;
            borrowableBook.borrowBook();

            System.out.println("Book borrowed using Borrowable interface.");
            System.out.println("Book available: " + book1.isAvailable());

        } catch (BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Searchable searchableBook = book1;

        if (searchableBook.searchBook("Java")) {
            System.out.println("Book found using Searchable interface.");
        } else {
            System.out.println("Book not found.");
        }

        // Collection Framework
        System.out.println("\n===== COLLECTION FRAMEWORK =====");

        Library library = new Library();

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        library.displayAllBooks();

        // Searching books using ArrayList
        library.searchBooks("Data");
    }
}