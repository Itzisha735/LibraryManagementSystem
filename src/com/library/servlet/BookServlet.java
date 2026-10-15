package com.library.servlet;

import com.library.dao.BookDAO;
import com.library.model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && session.getAttribute("userEmail") != null;
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && "ADMIN".equals(session.getAttribute("userRole"));
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect("login");
            return;
        }

        boolean admin = isAdmin(request);

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

        out.println("<h1>University Library Management System</h1>");
        out.println("<p>Logged in as: "
                + escapeHtml((String) request.getSession(false)
                        .getAttribute("userEmail"))
                + " | <a href='logout'>Log Out</a></p>");

        if (admin) {
            out.println("<div class='card'>");
            out.println("<h2>Add New Book</h2>");
            out.println("<form method='post' action='books'>");
            out.println("<input type='hidden' name='action' value='add'>");

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
        }

        out.println("<div class='card'>");
        out.println("<h2>Search Books</h2>");
        out.println("<form method='get' action='books'>");
        out.println("<div class='form-group'>");
        out.println("<label for='keyword'>Search by title or author</label>");
        out.println("<input type='text' id='keyword' name='keyword' value='"
                + escapeHtml(keyword) + "'>");
        out.println("</div>");
        out.println("<input type='submit' value='Search'>");
        out.println("</form>");

        if (keyword != null && !keyword.trim().isEmpty()) {
            out.println("<p>Search results for: <strong>"
                    + escapeHtml(keyword) + "</strong></p>");
            out.println("<p><a href='books'>Show All Books</a></p>");
        }
        out.println("</div>");

        out.println("<div class='card'>");
        out.println("<h2>Books</h2>");

        if (books.isEmpty()) {
            out.println("<p class='empty-message'>No books found.</p>");
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
                out.println("<td>" + escapeHtml(book.getTitle()) + "</td>");
                out.println("<td>" + escapeHtml(book.getAuthor()) + "</td>");

                if (book.isAvailable()) {
                    out.println("<td class='status-available'>Available</td>");
                } else {
                    out.println("<td class='status-borrowed'>Borrowed</td>");
                }

                out.println("<td>");

                if (book.isAvailable() || !book.isAvailable()) {
                    String action = book.isAvailable() ? "borrow" : "return";
                    String label = book.isAvailable() ? "Borrow" : "Return";

                    out.println("<form method='post' action='books' class='action-form'>");
                    out.println("<input type='hidden' name='action' value='" + action + "'>");
                    out.println("<input type='hidden' name='bookId' value='"
                            + book.getBookId() + "'>");
                    out.println("<input type='submit' value='" + label + "'>");
                    out.println("</form>");
                }

                if (admin) {
                    out.println("<form method='post' action='books' class='action-form'>");
                    out.println("<input type='hidden' name='action' value='delete'>");
                    out.println("<input type='hidden' name='bookId' value='"
                            + book.getBookId() + "'>");
                    out.println("<input type='submit' value='Delete'>");
                    out.println("</form>");
                }

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

        if (!isLoggedIn(request)) {
            response.sendRedirect("login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing action.");
            return;
        }

        boolean admin = isAdmin(request);

        if (("add".equals(action) || "delete".equals(action)) && !admin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only administrators can add or delete books.");
            return;
        }

        String bookIdValue = request.getParameter("bookId");
        int bookId;

        try {
            bookId = Integer.parseInt(bookIdValue);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID.");
            return;
        }

        switch (action) {
            case "delete":
                bookDAO.deleteBook(bookId);
                break;

            case "borrow":
                bookDAO.borrowBook(bookId);
                break;

            case "return":
                bookDAO.returnBook(bookId);
                break;

            case "add":
                String title = request.getParameter("title");
                String author = request.getParameter("author");

                if (title == null || title.trim().isEmpty()
                        || author == null || author.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Title and author are required.");
                    return;
                }

                Book book = new Book(
                        bookId, title.trim(), author.trim());
                bookDAO.addBook(book);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Unknown action.");
                return;
        }

        response.sendRedirect("books");
    }

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