package com.tripmate.controller;

import com.tripmate.dao.ChecklistDAO;
import com.tripmate.dao.TripDAO;
import com.tripmate.dao.TripMemberDAO;
import com.tripmate.model.ChecklistItem;
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
 * Controller handling Packing Checklist items (Add, Toggle Packed, Delete).
 */
@WebServlet(name = "ChecklistServlet", urlPatterns = {
        "/checklist-add",
        "/checklist-toggle",
        "/checklist-delete"
})
public class ChecklistServlet extends HttpServlet {

    private ChecklistDAO checklistDAO;
    private TripDAO tripDAO;
    private TripMemberDAO tripMemberDAO;

    @Override
    public void init() throws ServletException {
        this.checklistDAO = new ChecklistDAO();
        this.tripDAO = new TripDAO();
        this.tripMemberDAO = new TripMemberDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/checklist-toggle".equals(path)) {
            handleToggle(req, resp);
        } else if ("/checklist-delete".equals(path)) {
            handleDelete(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/checklist-add".equals(path)) {
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

        String itemName = req.getParameter("itemName");
        String category = req.getParameter("category");

        if (ValidationUtil.isNonEmpty(itemName)) {
            ChecklistItem item = new ChecklistItem(
                    tripId,
                    ValidationUtil.isNonEmpty(category) ? category : "MISCELLANEOUS",
                    itemName.trim()
            );
            checklistDAO.create(item);
        }

        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=checklist&success=Packing item added!");
    }

    private void handleToggle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String id = req.getParameter("id");
        boolean packed = Boolean.parseBoolean(req.getParameter("packed"));

        if (user == null || !canEditTrip(user, tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        checklistDAO.togglePacked(id, packed);
        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=checklist");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String id = req.getParameter("id");

        if (user == null || !canEditTrip(user, tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        checklistDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=checklist&success=Item removed");
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
