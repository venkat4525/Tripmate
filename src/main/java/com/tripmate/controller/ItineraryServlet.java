package com.tripmate.controller;

import com.tripmate.dao.ItineraryDAO;
import com.tripmate.dao.TripDAO;
import com.tripmate.dao.TripMemberDAO;
import com.tripmate.model.ItineraryItem;
import com.tripmate.model.Trip;
import com.tripmate.model.User;
import com.tripmate.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling Day-wise Itinerary actions (Add, Toggle, Delete).
 */
@WebServlet(name = "ItineraryServlet", urlPatterns = {
        "/itinerary-add",
        "/itinerary-toggle",
        "/itinerary-delete"
})
public class ItineraryServlet extends HttpServlet {

    private ItineraryDAO itineraryDAO;
    private TripDAO tripDAO;
    private TripMemberDAO tripMemberDAO;

    @Override
    public void init() throws ServletException {
        this.itineraryDAO = new ItineraryDAO();
        this.tripDAO = new TripDAO();
        this.tripMemberDAO = new TripMemberDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/itinerary-toggle".equals(path)) {
            handleToggle(req, resp);
        } else if ("/itinerary-delete".equals(path)) {
            handleDelete(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/itinerary-add".equals(path)) {
            handleAdd(req, resp);
        } else {
            doGet(req, resp);
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");

        if (user == null || !canEditTrip(user, tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        String dayNumberStr = req.getParameter("dayNumber");
        String timeSlot = req.getParameter("timeSlot");
        String activityTitle = req.getParameter("activityTitle");
        String location = req.getParameter("location");
        String costStr = req.getParameter("estimatedCost");
        String notes = req.getParameter("notes");

        int dayNumber = ValidationUtil.parseIntSafe(dayNumberStr, 1);
        double cost = ValidationUtil.parseDoubleSafe(costStr, 0.0);

        ItineraryItem item = new ItineraryItem();
        item.setTripId(tripId);
        item.setDayNumber(dayNumber);
        item.setTimeSlot(ValidationUtil.isNonEmpty(timeSlot) ? timeSlot.trim() : "Anytime");
        item.setActivityTitle(activityTitle != null ? activityTitle.trim() : "Activity");
        item.setLocation(location != null ? location.trim() : "");
        item.setEstimatedCost(cost);
        item.setNotes(notes != null ? notes.trim() : "");
        item.setCompleted(false);

        itineraryDAO.create(item);
        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=itinerary&success=Activity added!");
    }

    private void handleToggle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String id = req.getParameter("id");
        boolean completed = Boolean.parseBoolean(req.getParameter("completed"));

        if (user == null || !canEditTrip(user, tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        itineraryDAO.toggleCompletion(id, completed);
        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=itinerary");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String id = req.getParameter("id");

        if (user == null || !canEditTrip(user, tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        itineraryDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=itinerary&success=Activity removed");
    }

    private boolean canEditTrip(User user, String tripId) {
        if (user == null || tripId == null) return false;
        if (user.isAdmin()) return true;
        Trip trip = tripDAO.findById(tripId);
        if (trip != null && user.getId().equals(trip.getUserId())) return true;
        return tripMemberDAO.canEdit(tripId, user.getId());
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
