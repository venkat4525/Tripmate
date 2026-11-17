package com.tripmate.controller;

import com.tripmate.dao.ExpenseDAO;
import com.tripmate.dao.TripDAO;
import com.tripmate.dao.TripMemberDAO;
import com.tripmate.model.Expense;
import com.tripmate.model.Trip;
import com.tripmate.model.User;
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

/**
 * Controller handling Expense logging and deletion.
 */
@WebServlet(name = "ExpenseServlet", urlPatterns = {
        "/expense-add",
        "/expense-delete"
})
public class ExpenseServlet extends HttpServlet {

    private ExpenseDAO expenseDAO;
    private TripDAO tripDAO;
    private TripMemberDAO tripMemberDAO;

    @Override
    public void init() throws ServletException {
        this.expenseDAO = new ExpenseDAO();
        this.tripDAO = new TripDAO();
        this.tripMemberDAO = new TripMemberDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/expense-delete".equals(path)) {
            handleDelete(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/expense-add".equals(path)) {
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

        String title = req.getParameter("title");
        String amountStr = req.getParameter("amount");
        String category = req.getParameter("category");
        String paymentMethod = req.getParameter("paymentMethod");
        String expenseDateStr = req.getParameter("expenseDate");
        String notes = req.getParameter("notes");

        double amount = ValidationUtil.parseDoubleSafe(amountStr, 0.0);
        Date expenseDate = ValidationUtil.isNonEmpty(expenseDateStr) 
                ? DateUtil.parseInputDate(expenseDateStr) 
                : new Date();

        Expense expense = new Expense();
        expense.setTripId(tripId);
        expense.setPaidBy(user.getId());
        expense.setTitle(ValidationUtil.isNonEmpty(title) ? title.trim() : "Expense");
        expense.setCategory(ValidationUtil.isNonEmpty(category) ? category : "MISCELLANEOUS");
        expense.setAmount(amount);
        expense.setExpenseDate(expenseDate != null ? expenseDate : new Date());
        expense.setPaymentMethod(ValidationUtil.isNonEmpty(paymentMethod) ? paymentMethod : "CARD");
        expense.setNotes(notes != null ? notes.trim() : "");

        boolean created = expenseDAO.create(expense);

        if (created) {
            // Update actual expenses on trip
            double newTotal = expenseDAO.calculateTotalByTrip(tripId);
            tripDAO.updateActualExpenses(tripId, newTotal);
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=expenses&success=Expense recorded successfully!");
        } else {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=expenses&error=Failed to record expense");
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String id = req.getParameter("id");

        if (user == null || !canEditTrip(user, tripId)) {
            resp.sendRedirect(req.getContextPath() + "/trips?error=Unauthorized");
            return;
        }

        expenseDAO.delete(id);
        // Refresh total
        double newTotal = expenseDAO.calculateTotalByTrip(tripId);
        tripDAO.updateActualExpenses(tripId, newTotal);

        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=expenses&success=Expense removed");
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
