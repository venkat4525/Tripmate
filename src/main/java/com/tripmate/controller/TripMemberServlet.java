package com.tripmate.controller;

import com.tripmate.dao.TripDAO;
import com.tripmate.dao.TripMemberDAO;
import com.tripmate.dao.UserDAO;
import com.tripmate.model.Trip;
import com.tripmate.model.TripMember;
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
 * Controller managing group trip collaboration, invitations, and member roster.
 */
@WebServlet(name = "TripMemberServlet", urlPatterns = {
        "/trip-invite",
        "/trip-remove-member"
})
public class TripMemberServlet extends HttpServlet {

    private TripDAO tripDAO;
    private TripMemberDAO tripMemberDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        this.tripDAO = new TripDAO();
        this.tripMemberDAO = new TripMemberDAO();
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/trip-remove-member".equals(path)) {
            handleRemoveMember(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/trips");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/trip-invite".equals(path)) {
            handleInvite(req, resp);
        } else {
            doGet(req, resp);
        }
    }

    private void handleInvite(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String inviteeEmail = req.getParameter("inviteeEmail");
        String role = req.getParameter("role");

        if (user == null || tripId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Trip trip = tripDAO.findById(tripId);
        if (trip == null || (!user.getId().equals(trip.getUserId()) && !user.isAdmin())) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&error=Only trip owner can invite members");
            return;
        }

        if (!ValidationUtil.isValidEmail(inviteeEmail)) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=members&error=Invalid email address");
            return;
        }

        User inviteeUser = userDAO.findByEmail(inviteeEmail.trim());

        if (inviteeUser == null) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=members&error=User with email " + inviteeEmail + " is not registered yet on TripMate.");
            return;
        }

        // Add member
        TripMember member = new TripMember();
        member.setTripId(tripId);
        member.setUserId(inviteeUser.getId());
        member.setRole(ValidationUtil.isNonEmpty(role) ? role : "EDITOR");
        member.setStatus("ACTIVE");

        boolean added = tripMemberDAO.addMember(member);

        if (added) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=members&success=" + inviteeUser.getFullName() + " added to the trip!");
        } else {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=members&error=Failed to add member");
        }
    }

    private void handleRemoveMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getAuthenticatedUser(req);
        String tripId = req.getParameter("tripId");
        String memberId = req.getParameter("memberId");

        if (user == null || tripId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Trip trip = tripDAO.findById(tripId);
        if (trip == null || (!user.getId().equals(trip.getUserId()) && !user.isAdmin())) {
            resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&error=Only trip owner can remove members");
            return;
        }

        tripMemberDAO.removeMember(memberId);
        resp.sendRedirect(req.getContextPath() + "/trip-details?id=" + tripId + "&tab=members&success=Member removed");
    }

    private User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
