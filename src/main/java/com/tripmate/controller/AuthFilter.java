package com.tripmate.controller;

import com.tripmate.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to enforce authentication and role-based authorization across TripMate.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Allow static assets (css, js, images)
        if (path.startsWith("/assets/") || path.endsWith(".css") || path.endsWith(".js") 
                || path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".svg")) {
            chain.doFilter(request, response);
            return;
        }

        // Allow public pages
        if (path.equals("/") || path.equals("/home") || path.equals("/index.jsp")
                || path.equals("/login") || path.equals("/register")
                || path.equals("/destinations") || path.equals("/destination-details")) {
            chain.doFilter(request, response);
            return;
        }

        // Check active session
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        // If not logged in, redirect to login
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=Please log in to access this page");
            return;
        }

        // Protect Admin routes
        if (path.startsWith("/admin/")) {
            if (!currentUser.isAdmin()) {
                res.sendRedirect(req.getContextPath() + "/dashboard?error=Access denied: Administrator privileges required");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
