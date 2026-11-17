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
import java.util.Map;

/**
 * Controller generating a complete, clean, print-ready Trip Summary.
 */
@WebServlet(name = "TripSummaryServlet", urlPatterns = {"/trip-summary"})
public class TripSummaryServlet extends HttpServlet {

    private TripDAO tripDAO;
    private DestinationDAO destinationDAO;
    private ItineraryDAO itineraryDAO;
    private ExpenseDAO expenseDAO;
    private ChecklistDAO checklistDAO;
    private TripMemberDAO tripMemberDAO;

    @Override
    public void init() throws ServletException {
        this.tripDAO = new TripDAO();
        this.destinationDAO = new DestinationDAO();
        this.itineraryDAO = new ItineraryDAO();
        this.expenseDAO = new ExpenseDAO();
        this.checklistDAO = new ChecklistDAO();
        this.tripMemberDAO = new TripMemberDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
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

        Destination destination = null;
        if (trip.getDestinationId() != null) {
            destination = destinationDAO.findById(trip.getDestinationId());
        }

        List<ItineraryItem> itineraryItems = itineraryDAO.findByTripId(tripId);
        List<Expense> expenses = expenseDAO.findByTripId(tripId);
        double totalExpenses = expenseDAO.calculateTotalByTrip(tripId);
        Map<String, Double> expensesByCategory = expenseDAO.getExpensesByCategory(tripId);
        List<ChecklistItem> checklistItems = checklistDAO.findByTripId(tripId);
        List<TripMember> members = tripMemberDAO.findByTripId(tripId);

        req.setAttribute("trip", trip);
        req.setAttribute("destination", destination);
        req.setAttribute("itineraryItems", itineraryItems);
        req.setAttribute("expenses", expenses);
        req.setAttribute("totalExpenses", totalExpenses);
        req.setAttribute("expensesByCategory", expensesByCategory);
        req.setAttribute("checklistItems", checklistItems);
        req.setAttribute("members", members);
        req.setAttribute("pageTitle", "Trip Summary - " + trip.getTitle());

        req.getRequestDispatcher("/WEB-INF/views/user/trip-summary.jsp").forward(req, resp);
    }
}
