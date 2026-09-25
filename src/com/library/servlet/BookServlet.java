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

        List<Book> books = bookDAO.getAllBooks();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>University Library Management System</title>");
        out.println("</head>");

        out.println("<body>");

        out.println("<h1>University Library Management System</h1>");

        // Add Book form
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

        // Books list
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
            out.println("</tr>");

            for (Book book : books) {

                out.println("<tr>");

                out.println("<td>" + book.getBookId() + "</td>");
                out.println("<td>" + book.getTitle() + "</td>");
                out.println("<td>" + book.getAuthor() + "</td>");
                out.println("<td>" + book.isAvailable() + "</td>");

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

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");

        Book book = new Book(bookId, title, author);

        bookDAO.addBook(book);

        response.sendRedirect("books");
    }
}