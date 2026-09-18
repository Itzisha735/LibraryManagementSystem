package com.library.interfaces;

import com.library.exceptions.BookNotAvailableException;

public interface Borrowable {

    void borrowBook() throws BookNotAvailableException;

    void returnBook();
}