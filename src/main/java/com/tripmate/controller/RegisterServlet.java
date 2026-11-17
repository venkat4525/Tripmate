package com.tripmate.controller;

import com.tripmate.dao.UserDAO;
import com.tripmate.model.User;
import com.tripmate.util.PasswordUtil;
import com.tripmate.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller handling User Registration.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String phone = req.getParameter("phone");

        // Server-side validations
        if (!ValidationUtil.isNonEmpty(fullName) || !ValidationUtil.isNonEmpty(email) || !ValidationUtil.isNonEmpty(password)) {
            req.setAttribute("errorMessage", "All required fields must be filled.");
            retainFormData(req, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("errorMessage", "Please provide a valid email address.");
            retainFormData(req, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            retainFormData(req, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Passwords do not match.");
            retainFormData(req, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        // Check if email already exists
        if (userDAO.findByEmail(email) != null) {
            req.setAttribute("errorMessage", "An account with this email already exists. Please log in.");
            retainFormData(req, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        // Hash password securely with BCrypt
        String hashedPassword = PasswordUtil.hashPassword(password);

        User newUser = new User();
        newUser.setFullName(fullName.trim());
        newUser.setEmail(email.trim().toLowerCase());
        newUser.setPassword(hashedPassword);
        newUser.setPhone(phone != null ? phone.trim() : "");
        newUser.setRole("USER");
        newUser.setProfileImage("assets/images/profiles/default.png");
        newUser.setActive(true);

        boolean created = userDAO.create(newUser);

        if (created) {
            resp.sendRedirect(req.getContextPath() + "/login?success=Registration successful! You can now log in.");
        } else {
            req.setAttribute("errorMessage", "Registration failed due to a database error. Please try again.");
            retainFormData(req, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }

    private void retainFormData(HttpServletRequest req, String fullName, String email, String phone) {
        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);
    }
}
