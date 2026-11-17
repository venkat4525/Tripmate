package com.tripmate.controller;

import com.tripmate.dao.*;
import com.tripmate.model.*;
import com.tripmate.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller managing Administrator operations (Dashboard, Users, Destinations, Categories).
 */
@WebServlet(name = "AdminServlet", urlPatterns = {
        "/admin/dashboard",
        "/admin/users",
        "/admin/user-toggle",
        "/admin/destinations",
        "/admin/destination-delete",
        "/admin/categories"
})
public class AdminServlet extends HttpServlet {

    private UserDAO userDAO;
    private DestinationDAO destinationDAO;
    private CategoryDAO categoryDAO;
    private TripDAO tripDAO;

    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAO();
        this.destinationDAO = new DestinationDAO();
        this.categoryDAO = new CategoryDAO();
        this.tripDAO = new TripDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/admin/dashboard":
                handleAdminDashboard(req, resp);
                break;
            case "/admin/users":
                handleUsersList(req, resp);
                break;
            case "/admin/user-toggle":
                handleUserToggle(req, resp);
                break;
            case "/admin/destinations":
                handleDestinationsList(req, resp);
                break;
            case "/admin/destination-delete":
                handleDestinationDelete(req, resp);
                break;
            case "/admin/categories":
                handleCategoriesList(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                break;
        }
    }

    private void handleAdminDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long totalUsers = userDAO.countUsers();
        long totalDestinations = destinationDAO.countDestinations();
        long totalTrips = tripDAO.countTrips();
        long totalCategories = categoryDAO.countCategories();

        List<User> recentUsers = userDAO.findAll();
        List<Trip> recentTrips = tripDAO.findAll();

        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalDestinations", totalDestinations);
        req.setAttribute("totalTrips", totalTrips);
        req.setAttribute("totalCategories", totalCategories);
        req.setAttribute("recentUsers", recentUsers);
        req.setAttribute("recentTrips", recentTrips);
        req.setAttribute("activeSidebar", "admin-dashboard");
        req.setAttribute("pageTitle", "Admin Dashboard - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    private void handleUsersList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        req.setAttribute("users", users);
        req.setAttribute("activeSidebar", "admin-users");
        req.setAttribute("pageTitle", "Manage Users - TripMate Admin");

        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    private void handleUserToggle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("id");
        if (ValidationUtil.isNonEmpty(userId)) {
            User user = userDAO.findById(userId);
            if (user != null) {
                user.setActive(!user.isActive());
                userDAO.update(user);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users?success=User status updated successfully");
    }

    private void handleDestinationsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Destination> destinations = destinationDAO.findAll();
        List<Category> categories = categoryDAO.findAll();

        req.setAttribute("destinations", destinations);
        req.setAttribute("categories", categories);
        req.setAttribute("activeSidebar", "admin-destinations");
        req.setAttribute("pageTitle", "Manage Destinations - TripMate Admin");

        req.getRequestDispatcher("/WEB-INF/views/admin/destinations.jsp").forward(req, resp);
    }

    private void handleDestinationDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        if (ValidationUtil.isNonEmpty(id)) {
            destinationDAO.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/destinations?success=Destination deleted successfully");
    }

    private void handleCategoriesList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        req.setAttribute("categories", categories);
        req.setAttribute("activeSidebar", "admin-categories");
        req.setAttribute("pageTitle", "Manage Categories - TripMate Admin");

        req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, resp);
    }
}
