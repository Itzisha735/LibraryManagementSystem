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

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        BookDAO bookDAO = new BookDAO();

        List<Book> books = bookDAO.getAllBooks();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Library Books</title>");
        out.println("</head>");

        out.println("<body>");

        out.println("<h1>University Library Management System</h1>");
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
}