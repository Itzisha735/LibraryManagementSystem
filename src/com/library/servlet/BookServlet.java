package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String keyword = request.getParameter("keyword");

        List<Book> books;

        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchBooksForWeb(keyword.trim());
        } else {
            books = bookDAO.getAllBooks();
        }

        out.println("<html>");
        out.println("<head>");
        out.println("<title>University Library Management System</title>");
        out.println("</head>");

        out.println("<body>");

        out.println("<h1>University Library Management System</h1>");

        // =========================
        // ADD BOOK FORM
        // =========================

        out.println("<h2>Add New Book</h2>");

        out.println("<form method='post' action='books'>");

        out.println("Book ID: ");
        out.println("<input type='number' name='bookId' required>");
        out.println("<br><br>");

        out.println("Title: ");
        out.println("<input type='text' name='title' required>");
        out.println("<br><br>");

        out.println("Author: ");
        out.println("<input type='text' name='author' required>");
        out.println("<br><br>");

        out.println("<input type='submit' value='Add Book'>");

        out.println("</form>");

        // =========================
        // SEARCH BOOKS
        // =========================

        out.println("<h2>Search Books</h2>");

        out.println("<form method='get' action='books'>");

        out.println("Keyword: ");
        out.println("<input type='text' name='keyword' required>");

        out.println("<input type='submit' value='Search'>");

        out.println("</form>");

        if (keyword != null && !keyword.trim().isEmpty()) {

            out.println("<p>Search results for: <strong>"
                    + keyword
                    + "</strong></p>");

            out.println("<p><a href='books'>Show All Books</a></p>");
        }

        // =========================
        // BOOK LIST
        // =========================

        out.println("<h2>Available Books</h2>");

        if (books.isEmpty()) {

            out.println("<p>No books found.</p>");

        } else {

            out.println("<table border='1'>");

            out.println("<tr>");
            out.println("<th>Book ID</th>");
            out.println("<th>Title</th>");
            out.println("<th>Author</th>");
            out.println("<th>Available</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");

            for (Book book : books) {

                out.println("<tr>");

                out.println("<td>" + book.getBookId() + "</td>");
                out.println("<td>" + book.getTitle() + "</td>");
                out.println("<td>" + book.getAuthor() + "</td>");
                out.println("<td>" + book.isAvailable() + "</td>");

                // =========================
                // ACTION BUTTONS
                // =========================

                out.println("<td>");

                // Borrow / Return button
                out.println("<form method='post' action='books'>");

                out.println(
                    "<input type='hidden' name='bookId' value='"
                    + book.getBookId()
                    + "'>"
                );

                if (book.isAvailable()) {

                    out.println(
                        "<input type='hidden' name='action' value='borrow'>"
                    );

                    out.println(
                        "<input type='submit' value='Borrow'>"
                    );

                } else {

                    out.println(
                        "<input type='hidden' name='action' value='return'>"
                    );

                    out.println(
                        "<input type='submit' value='Return'>"
                    );
                }

                out.println("</form>");

                // Delete button
                out.println("<form method='post' action='books'>");

                out.println(
                    "<input type='hidden' name='action' value='delete'>"
                );

                out.println(
                    "<input type='hidden' name='bookId' value='"
                    + book.getBookId()
                    + "'>"
                );

                out.println(
                    "<input type='submit' value='Delete'>"
                );

                out.println("</form>");

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");
        }

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // =========================
        // BORROW BOOK
        // =========================

        if ("borrow".equals(action)) {

            int bookId = Integer.parseInt(
                    request.getParameter("bookId")
            );

            bookDAO.borrowBook(bookId);

            response.sendRedirect("books");

            return;
        }

        // =========================
        // RETURN BOOK
        // =========================

        if ("return".equals(action)) {

            int bookId = Integer.parseInt(
                    request.getParameter("bookId")
            );

            bookDAO.returnBook(bookId);

            response.sendRedirect("books");

            return;
        }

        // =========================
        // DELETE BOOK
        // =========================

        if ("delete".equals(action)) {

            int bookId = Integer.parseInt(
                    request.getParameter("bookId")
            );

            bookDAO.deleteBook(bookId);

            response.sendRedirect("books");

            return;
        }

        // =========================
        // ADD BOOK
        // =========================

        int bookId = Integer.parseInt(
                request.getParameter("bookId")
        );

        String title = request.getParameter("title");
        String author = request.getParameter("author");

        Book book = new Book(bookId, title, author);

        bookDAO.addBook(book);

        response.sendRedirect("books");
    }
}