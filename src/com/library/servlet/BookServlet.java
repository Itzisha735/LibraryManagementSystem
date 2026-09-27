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

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String keyword = request.getParameter("keyword");

        List<Book> books;

        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchBooksForWeb(keyword.trim());
        } else {
            books = bookDAO.getAllBooks();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>University Library Management System</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");

        out.println("<body>");

        out.println("<div class='container'>");

        // =========================
        // HEADER
        // =========================

        out.println("<h1>University Library Management System</h1>");

        // =========================
        // ADD BOOK
        // =========================

        out.println("<div class='card'>");

        out.println("<h2>Add New Book</h2>");

        out.println("<form method='post' action='books'>");

        out.println("<div class='form-group'>");
        out.println("<label for='bookId'>Book ID</label>");
        out.println("<input type='number' id='bookId' name='bookId' required>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='title'>Title</label>");
        out.println("<input type='text' id='title' name='title' required>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='author'>Author</label>");
        out.println("<input type='text' id='author' name='author' required>");
        out.println("</div>");

        out.println("<input type='submit' value='Add Book'>");

        out.println("</form>");

        out.println("</div>");

        // =========================
        // SEARCH BOOKS
        // =========================

        out.println("<div class='card'>");

        out.println("<h2>Search Books</h2>");

        out.println("<form method='get' action='books'>");

        out.println("<div class='form-group'>");
        out.println("<label for='keyword'>Search by title or author</label>");

        out.println(
            "<input type='text' id='keyword' name='keyword' "
            + "value='"
            + escapeHtml(keyword)
            + "'>"
        );

        out.println("</div>");

        out.println("<input type='submit' value='Search'>");

        out.println("</form>");

        if (keyword != null && !keyword.trim().isEmpty()) {

            out.println(
                "<p>Search results for: <strong>"
                + escapeHtml(keyword)
                + "</strong></p>"
            );

            out.println("<p><a href='books'>Show All Books</a></p>");
        }

        out.println("</div>");

        // =========================
        // BOOK LIST
        // =========================

        out.println("<div class='card'>");

        out.println("<h2>Books</h2>");

        if (books.isEmpty()) {

            out.println(
                "<p class='empty-message'>No books found.</p>"
            );

        } else {

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>Book ID</th>");
            out.println("<th>Title</th>");
            out.println("<th>Author</th>");
            out.println("<th>Status</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");

            for (Book book : books) {

                out.println("<tr>");

                out.println("<td>" + book.getBookId() + "</td>");

                out.println(
                    "<td>"
                    + escapeHtml(book.getTitle())
                    + "</td>"
                );

                out.println(
                    "<td>"
                    + escapeHtml(book.getAuthor())
                    + "</td>"
                );

                // =========================
                // STATUS
                // =========================

                if (book.isAvailable()) {

                    out.println(
                        "<td class='status-available'>Available</td>"
                    );

                } else {

                    out.println(
                        "<td class='status-borrowed'>Borrowed</td>"
                    );
                }

                // =========================
                // ACTIONS
                // =========================

                out.println("<td>");

                if (book.isAvailable()) {

                    out.println("<form method='post' action='books' class='action-form'>");

                    out.println(
                        "<input type='hidden' name='action' value='borrow'>"
                    );

                    out.println(
                        "<input type='hidden' name='bookId' value='"
                        + book.getBookId()
                        + "'>"
                    );

                    out.println(
                        "<input type='submit' value='Borrow'>"
                    );

                    out.println("</form>");

                } else {

                    out.println("<form method='post' action='books' class='action-form'>");

                    out.println(
                        "<input type='hidden' name='action' value='return'>"
                    );

                    out.println(
                        "<input type='hidden' name='bookId' value='"
                        + book.getBookId()
                        + "'>"
                    );

                    out.println(
                        "<input type='submit' value='Return'>"
                    );

                    out.println("</form>");
                }

                // Delete button

                out.println("<form method='post' action='books' class='action-form'>");

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

        out.println("</div>");

        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        int bookId = Integer.parseInt(
                request.getParameter("bookId")
        );

        // =========================
        // DELETE
        // =========================

        if ("delete".equals(action)) {

            bookDAO.deleteBook(bookId);

            response.sendRedirect("books");

            return;
        }

        // =========================
        // BORROW
        // =========================

        if ("borrow".equals(action)) {

            bookDAO.borrowBook(bookId);

            response.sendRedirect("books");

            return;
        }

        // =========================
        // RETURN
        // =========================

        if ("return".equals(action)) {

            bookDAO.returnBook(bookId);

            response.sendRedirect("books");

            return;
        }

        // =========================
        // ADD BOOK
        // =========================

        String title = request.getParameter("title");
        String author = request.getParameter("author");

        Book book = new Book(bookId, title, author);

        bookDAO.addBook(book);

        response.sendRedirect("books");
    }

    // =========================
    // HTML ESCAPING
    // =========================

    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}