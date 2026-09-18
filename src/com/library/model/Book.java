package com.library.model;

import com.library.exceptions.BookNotAvailableException;
import com.library.interfaces.Borrowable;
import com.library.interfaces.Searchable;

public class Book implements Borrowable, Searchable {

    private int bookId;
    private String title;
    private String author;
    private boolean available;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public void borrowBook() throws BookNotAvailableException {

        if (!available) {
            throw new BookNotAvailableException(
                    "Book is already borrowed: " + title
            );
        }

        available = false;
    }

    @Override
    public void returnBook() {
        available = true;
    }

    @Override
    public boolean searchBook(String keyword) {
        return title.toLowerCase().contains(keyword.toLowerCase())
                || author.toLowerCase().contains(keyword.toLowerCase());
    }

    public void displayBookDetails() {
        System.out.println("Book ID: " + bookId);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Available: " + available);
    }
}