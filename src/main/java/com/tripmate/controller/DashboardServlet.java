package com.tripmate.controller;

import com.tripmate.dao.*;
import com.tripmate.model.Notification;
import com.tripmate.model.Trip;
import com.tripmate.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Main User Dashboard.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private TripDAO tripDAO;
    private TripMemberDAO tripMemberDAO;
    private ExpenseDAO expenseDAO;
    private FavouriteDAO favouriteDAO;
    private NotificationDAO notificationDAO;
    private DestinationDAO destinationDAO;

    @Override
    public void init() throws ServletException {
        this.tripDAO = new TripDAO();
        this.tripMemberDAO = new TripMemberDAO();
        this.expenseDAO = new ExpenseDAO();
        this.favouriteDAO = new FavouriteDAO();
        this.notificationDAO = new NotificationDAO();
        this.destinationDAO = new DestinationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please sign in to access your dashboard");
            return;
        }

        String userId = currentUser.getId();

        // 1. Fetch user's own trips
        List<Trip> ownedTrips = tripDAO.findByUserId(userId);

        // 2. Fetch trips shared with the user
        List<String> sharedTripIds = tripMemberDAO.findTripIdsByUserId(userId);
        List<Trip> sharedTrips = new ArrayList<>();
        for (String tid : sharedTripIds) {
            Trip t = tripDAO.findById(tid);
            // Only add if not already owned by user
            if (t != null && !userId.equals(t.getUserId())) {
                sharedTrips.add(t);
            }
        }

        // Combine for recent trips display
        List<Trip> allUserTrips = new ArrayList<>(ownedTrips);
        allUserTrips.addAll(sharedTrips);

        // 3. Calculate statistics
        int totalTripsCount = allUserTrips.size();
        long planningCount = allUserTrips.stream().filter(t -> "PLANNING".equalsIgnoreCase(t.getStatus())).count();
        long ongoingCount = allUserTrips.stream().filter(t -> "ONGOING".equalsIgnoreCase(t.getStatus())).count();

        double totalExpenses = 0.0;
        for (Trip t : ownedTrips) {
            totalExpenses += expenseDAO.calculateTotalByTrip(t.getId());
        }

        int favouritesCount = favouriteDAO.findByUserId(userId).size();

        // 4. Notifications
        List<Notification> notifications = notificationDAO.findByUserId(userId);
        long unreadCount = notificationDAO.countUnread(userId);

        req.setAttribute("ownedTrips", ownedTrips);
        req.setAttribute("sharedTrips", sharedTrips);
        req.setAttribute("recentTrips", allUserTrips);
        req.setAttribute("totalTripsCount", totalTripsCount);
        req.setAttribute("activeTripsCount", planningCount + ongoingCount);
        req.setAttribute("totalExpenses", totalExpenses);
        req.setAttribute("favouritesCount", favouritesCount);
        req.setAttribute("notifications", notifications);
        req.setAttribute("unreadCount", unreadCount);
        req.setAttribute("activeSidebar", "dashboard");
        req.setAttribute("pageTitle", "Dashboard - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(req, resp);
    }
}
