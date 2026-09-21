package com.library;

import com.library.dao.BookDAO;

public class BookDAOTest {

    public static void main(String[] args) {

        BookDAO bookDAO = new BookDAO();

        // View books before deletion
        bookDAO.viewAllBooks();

        // Delete Book ID 105
        bookDAO.deleteBook(105);

        // View books after deletion
        bookDAO.viewAllBooks();
    }
}