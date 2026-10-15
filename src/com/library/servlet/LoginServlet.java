package com.library.servlet;

import com.library.dao.UserDAO;
import com.library.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Library Login</title>");
        out.println("<link rel='stylesheet' href='css/style.css'>");
        out.println("</head><body>");

        out.println("<div class='container'>");
        out.println("<div class='card'>");
        out.println("<h1>Library Login</h1>");

        if ("1".equals(request.getParameter("error"))) {
            out.println("<p class='status-borrowed'>Invalid email or password.</p>");
        }

        out.println("<form method='post' action='login'>");

        out.println("<div class='form-group'>");
        out.println("<label for='email'>Email</label>");
        out.println("<input type='email' id='email' name='email' required>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='password'>Password</label>");
        out.println("<input type='password' id='password' name='password' required>");
        out.println("</div>");

        out.println("<input type='submit' value='Log In'>");
        out.println("</form>");

        out.println("<p><a href='index.jsp'>Back to Home</a></p>");
        out.println("</div></div></body></html>");
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null
                || email.trim().isEmpty() || password.isEmpty()) {
            response.sendRedirect("login?error=1");
            return;
        }

        String cleanEmail = email.trim();

        try {
            if (userDAO.verifyLogin(cleanEmail, password)) {

                User user = userDAO.findUserByEmail(cleanEmail);

                if (user == null || user.getRole() == null) {
                    response.sendRedirect("login?error=1");
                    return;
                }

                HttpSession oldSession = request.getSession(false);

                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());

                response.sendRedirect("books");

            } else {
                response.sendRedirect("login?error=1");
            }

        } catch (SQLException | GeneralSecurityException e) {
            throw new ServletException("Login could not be completed.", e);
        }
    }
}