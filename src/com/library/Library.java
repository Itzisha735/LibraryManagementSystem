package com.library;

import java.util.ArrayList;
import com.library.model.Book;

public class Library {

    private ArrayList<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }

    public void displayAllBooks() {

        System.out.println("\n===== ALL BOOKS =====");

        for (Book book : books) {
            book.displayBookDetails();
            System.out.println("--------------------");
        }
    }

    public void searchBooks(String keyword) {

        System.out.println("\n===== SEARCH RESULTS =====");

        boolean found = false;

        for (Book book : books) {

            if (book.searchBook(keyword)) {
                book.displayBookDetails();
                System.out.println("--------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found.");
        }
    }
}