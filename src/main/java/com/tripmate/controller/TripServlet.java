package com.tripmate.controller;

import com.tripmate.dao.*;
import com.tripmate.model.*;
import com.tripmate.util.DateUtil;
import com.tripmate.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller managing Trip Creation, Listing, Details Hub, Updating, and Deletion.
 */
@WebServlet(name = "TripServlet", urlPatterns = {
        "/trips",
        "/trip-create",
        "/trip-details",
        "/trip-edit",
        "/trip-delete"
})
public class TripServlet extends HttpServlet {

    private TripDAO tripDAO;
    private DestinationDAO destinationDAO;
    private TripMemberDAO tripMemberDAO;
    private ItineraryDAO itineraryDAO;
    private ExpenseDAO expenseDAO;
    private ChecklistDAO checklistDAO;

    @Override
    public void init() throws ServletException {
        this.tripDAO = new TripDAO();
        this.destinationDAO = new DestinationDAO();
        this.tripMemberDAO = new TripMemberDAO();
        this.itineraryDAO = new ItineraryDAO();
        this.expenseDAO = new ExpenseDAO();
        this.checklistDAO = new ChecklistDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/trips":
                handleTripsList(req, resp);
                break;
            case "/trip-create":
                handleTripCreateForm(req, resp);
                break;
            case "/trip-details":
                handleTripDetails(req, resp);
                break;
            case "/trip-edit":
                handleTripEditForm(req, resp);
                break;
            case "/trip-delete":
                handleTripDelete(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/trips");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/trip-create".equals(path)) {
            processTripCreate(req, resp);
        } else if ("/trip-edit".equals(path)) {
            processTripEdit(req, resp);
        } else {
            doGet(req, resp);
        }
    }

    private void handleTripsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please log in to view trips");
            return;
        }

        List<Trip> ownedTrips = tripDAO.findByUserId(user.getId());

        // Fetch shared trips
        List<String> sharedTripIds = tripMemberDAO.findTripIdsByUserId(user.getId());
        List<Trip> sharedTrips = new java.util.ArrayList<>();
        for (String tid : sharedTripIds) {
            Trip t = tripDAO.findById(tid);
            if (t != null && !user.getId().equals(t.getUserId())) {
                sharedTrips.add(t);
            }
        }

        req.setAttribute("ownedTrips", ownedTrips);
        req.setAttribute("sharedTrips", sharedTrips);
        req.setAttribute("activeNav", "trips");
        req.setAttribute("activeSidebar", "trips");
        req.setAttribute("pageTitle", "My Trips - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/user/trips.jsp").forward(req, resp);
    }

    private void handleTripCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please log in to create a trip");
            return;
        }

        List<Destination> destinations = destinationDAO.findAll();
        String preselectedDestId = req.getParameter("destId");

        req.setAttribute("destinations", destinations);
        req.setAttribute("preselectedDestId", preselectedDestId);
        req.setAttribute("activeSidebar", "trip-create");
        req.setAttribute("pageTitle", "Plan New Trip - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/user/trip-create.jsp").forward(req, resp);
    }

    private void processTripCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please log in to create a trip");
            return;
        }

        String title = req.getParameter("title");
        String destinationId = req.getParameter("destinationId");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        String budgetStr = req.getParameter("estimatedBudget");
        String visibility = req.getParameter("visibility");
        String notes = req.getParameter("notes");

        if (!ValidationUtil.isNonEmpty(title) || !ValidationUtil.isNonEmpty(destinationId) 
                || !ValidationUtil.isNonEmpty(startDateStr) || !ValidationUtil.isNonEmpty(endDateStr)) {
            req.setAttribute("errorMessage", "Trip title, destination, and dates are required.");
            handleTripCreateForm(req, resp);
            return;
        }

        Date startDate = DateUtil.parseInputDate(startDateStr);
        Date endDate = DateUtil.parseInputDate(endDateStr);

        if (startDate == null || endDate == null || endDate.before(startDate)) {
            req.setAttribute("errorMessage", "End date must be on or after start date.");
            handleTripCreateForm(req, resp);
            return;
        }

        Destination destination = destinationDAO.findById(destinationId);
        String destinationName = (destination != null) ? destination.getName() : "Custom Destination";

        double estimatedBudget = ValidationUtil.parseDoubleSafe(budgetStr, 0.0);

        Trip trip = new Trip();
        trip.setUserId(user.getId());
        trip.setTitle(title.trim());
        trip.setDestinationId(destinationId);
        trip.setDestinationName(destinationName);
        trip.setStartDate(startDate);
        trip.setEndDate(endDate);
        trip.setEstimatedBudget(estimatedBudget);
        trip.setActualExpenses(0.0);
        trip.setStatus("PLANNING");
        trip.setVisibility(ValidationUtil.isNonEmpty(visibility) ? visibility : "PRIVATE");
        trip.setNotes(notes != null ? notes.trim() : "");

        boolean created = tripDAO.create(trip);

        if (created) {
            // Automatically add creator as OWNER in trip_members
            TripMember ownerMember = new TripMember(trip.getId(), user.getId(), "OWNER", "ACTIVE");
            tripMemberDAO.addMember(ownerMember);

            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + trip.getId() + "&success=Trip created successfully!");
        } else {
            req.setAttribute("errorMessage", "Failed to create trip due to a database error.");
            handleTripCreateForm(req, resp);
        }
    }

    private void handleTripDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please log in to view trip details");
            return;
        }

        String tripId = req.getParameter("id");
        if (!ValidationUtil.isNonEmpty(tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips");
            return;
        }

        Trip trip = tripDAO.findById(tripId);
        if (trip == null) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Trip not found");
            return;
        }

        // Verify access: user must be owner, member, or admin
        boolean isOwner = user.getId().equals(trip.getUserId()) || user.isAdmin();
        TripMember member = tripMemberDAO.findMembership(tripId, user.getId());

        if (!isOwner && member == null) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=You do not have permission to view this trip");
            return;
        }

        boolean canEdit = isOwner || (member != null && "EDITOR".equalsIgnoreCase(member.getRole()));

        // Fetch Itinerary items
        List<ItineraryItem> itineraryItems = itineraryDAO.findByTripId(tripId);

        // Fetch Expenses & Aggregations
        List<Expense> expenses = expenseDAO.findByTripId(tripId);
        double totalExpenses = expenseDAO.calculateTotalByTrip(tripId);
        trip.setActualExpenses(totalExpenses);
        tripDAO.updateActualExpenses(tripId, totalExpenses); // Keep sync in DB
        Map<String, Double> expensesByCategory = expenseDAO.getExpensesByCategory(tripId);

        // Fetch Checklist items
        List<ChecklistItem> checklistItems = checklistDAO.findByTripId(tripId);

        // Fetch Trip Members
        List<TripMember> members = tripMemberDAO.findByTripId(tripId);

        // Fetch Destination details if exists
        Destination destination = null;
        if (trip.getDestinationId() != null) {
            destination = destinationDAO.findById(trip.getDestinationId());
        }

        String activeTab = req.getParameter("tab");
        if (!ValidationUtil.isNonEmpty(activeTab)) {
            activeTab = "itinerary";
        }

        req.setAttribute("trip", trip);
        req.setAttribute("destination", destination);
        req.setAttribute("itineraryItems", itineraryItems);
        req.setAttribute("expenses", expenses);
        req.setAttribute("totalExpenses", totalExpenses);
        req.setAttribute("expensesByCategory", expensesByCategory);
        req.setAttribute("checklistItems", checklistItems);
        req.setAttribute("members", members);
        req.setAttribute("isOwner", isOwner);
        req.setAttribute("canEdit", canEdit);
        req.setAttribute("activeTab", activeTab);
        req.setAttribute("activeNav", "trips");
        req.setAttribute("pageTitle", trip.getTitle() + " - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/user/trip-details.jsp").forward(req, resp);
    }

    private void handleTripEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String tripId = req.getParameter("id");
        Trip trip = tripDAO.findById(tripId);

        if (trip == null) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Trip not found");
            return;
        }

        // Only owner or admin can edit master trip settings
        if (!user.getId().equals(trip.getUserId()) && !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&error=Only trip owner can edit settings");
            return;
        }

        List<Destination> destinations = destinationDAO.findAll();
        req.setAttribute("trip", trip);
        req.setAttribute("destinations", destinations);
        req.setAttribute("pageTitle", "Edit Trip - " + trip.getTitle());

        req.getRequestDispatcher("/WEB-INF/views/user/trip-edit.jsp").forward(req, resp);
    }

    private void processTripEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String tripId = req.getParameter("id");
        Trip trip = tripDAO.findById(tripId);

        if (trip == null || (!user.getId().equals(trip.getUserId()) && !user.isAdmin())) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        String title = req.getParameter("title");
        String destinationId = req.getParameter("destinationId");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        String budgetStr = req.getParameter("estimatedBudget");
        String status = req.getParameter("status");
        String visibility = req.getParameter("visibility");
        String notes = req.getParameter("notes");

        Date startDate = DateUtil.parseInputDate(startDateStr);
        Date endDate = DateUtil.parseInputDate(endDateStr);

        trip.setTitle(title != null ? title.trim() : trip.getTitle());
        trip.setDestinationId(destinationId);
        if (destinationId != null) {
            Destination d = destinationDAO.findById(destinationId);
            if (d != null) trip.setDestinationName(d.getName());
        }
        if (startDate != null) trip.setStartDate(startDate);
        if (endDate != null) trip.setEndDate(endDate);
        trip.setEstimatedBudget(ValidationUtil.parseDoubleSafe(budgetStr, trip.getEstimatedBudget()));
        trip.setStatus(ValidationUtil.isNonEmpty(status) ? status : trip.getStatus());
        trip.setVisibility(ValidationUtil.isNonEmpty(visibility) ? visibility : trip.getVisibility());
        trip.setNotes(notes != null ? notes.trim() : "");

        boolean updated = tripDAO.update(trip);

        if (updated) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&success=Trip updated successfully!");
        } else {
            resp.sendRedirect(req.getContextPath() + "/trip-edit?id=" + tripId + "&error=Failed to update trip");
        }
    }

    private void handleTripDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String tripId = req.getParameter("id");
        Trip trip = tripDAO.findById(tripId);

        if (trip == null) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Trip not found");
            return;
        }

        // Ownership verification
        if (!user.getId().equals(trip.getUserId()) && !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&error=Only trip owner can delete this trip");
            return;
        }

        // Cascade delete child collections
        itineraryDAO.deleteByTripId(tripId);
        expenseDAO.deleteByTripId(tripId);
        checklistDAO.deleteByTripId(tripId);
        tripDAO.delete(tripId);

        resp.sendRedirect(req.getContextPath() + "/trips?success=Trip and all associated records deleted successfully");
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
