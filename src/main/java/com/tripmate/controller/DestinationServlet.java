package com.tripmate.controller;

import com.tripmate.dao.CategoryDAO;
import com.tripmate.dao.DestinationDAO;
import com.tripmate.dao.FavouriteDAO;
import com.tripmate.model.Category;
import com.tripmate.model.Destination;
import com.tripmate.model.User;
import com.tripmate.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for Destination Discovery, Filtering, Details, and Favourites.
 */
@WebServlet(name = "DestinationServlet", urlPatterns = {
        "/destinations",
        "/destination-details",
        "/favourites",
        "/toggle-favourite"
})
public class DestinationServlet extends HttpServlet {

    private DestinationDAO destinationDAO;
    private CategoryDAO categoryDAO;
    private FavouriteDAO favouriteDAO;

    @Override
    public void init() throws ServletException {
        this.destinationDAO = new DestinationDAO();
        this.categoryDAO = new CategoryDAO();
        this.favouriteDAO = new FavouriteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/destinations":
                handleDestinationsList(req, resp);
                break;
            case "/destination-details":
                handleDestinationDetails(req, resp);
                break;
            case "/favourites":
                handleFavouritesList(req, resp);
                break;
            case "/toggle-favourite":
                handleToggleFavourite(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/destinations");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private void handleDestinationsList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        String categoryId = req.getParameter("category");
        String country = req.getParameter("country");
        String maxCostStr = req.getParameter("maxCost");
        Double maxCost = ValidationUtil.isNonEmpty(maxCostStr) ? ValidationUtil.parseDoubleSafe(maxCostStr, 0.0) : null;

        List<Destination> destinations;
        if (ValidationUtil.isNonEmpty(query) || ValidationUtil.isNonEmpty(categoryId) 
                || ValidationUtil.isNonEmpty(country) || (maxCost != null && maxCost > 0)) {
            destinations = destinationDAO.search(query, categoryId, maxCost, country);
        } else {
            destinations = destinationDAO.findAll();
        }

        // Attach category names
        List<Category> categories = categoryDAO.findAll();
        for (Destination d : destinations) {
            if (d.getCategoryId() != null) {
                Category c = categoryDAO.findById(d.getCategoryId());
                if (c != null) d.setCategoryName(c.getName());
            }
        }

        // Get user's favourited destination IDs for quick UI rendering
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        Set<String> favouriteIds = new HashSet<>();
        if (currentUser != null) {
            favouriteDAO.findByUserId(currentUser.getId()).forEach(f -> favouriteIds.add(f.getDestinationId()));
        }

        req.setAttribute("destinations", destinations);
        req.setAttribute("categories", categories);
        req.setAttribute("favouriteIds", favouriteIds);
        req.setAttribute("paramQ", query);
        req.setAttribute("paramCategory", categoryId);
        req.setAttribute("paramCountry", country);
        req.setAttribute("paramMaxCost", maxCostStr);
        req.setAttribute("activeNav", "destinations");
        req.setAttribute("pageTitle", "Explore Destinations - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/destinations/destinations.jsp").forward(req, resp);
    }

    private void handleDestinationDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (!ValidationUtil.isNonEmpty(id)) {
            resp.sendRedirect(req.getContextPath() + "/destinations");
            return;
        }

        Destination destination = destinationDAO.findById(id);
        if (destination == null) {
            resp.sendRedirect(req.getContextPath() + "/destinations?error=Destination not found");
            return;
        }

        if (destination.getCategoryId() != null) {
            Category c = categoryDAO.findById(destination.getCategoryId());
            if (c != null) destination.setCategoryName(c.getName());
        }

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        boolean isFavourited = false;
        if (currentUser != null) {
            isFavourited = favouriteDAO.isFavourited(currentUser.getId(), id);
        }

        req.setAttribute("destination", destination);
        req.setAttribute("isFavourited", isFavourited);
        req.setAttribute("activeNav", "destinations");
        req.setAttribute("pageTitle", destination.getName() + " - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/destinations/destination-details.jsp").forward(req, resp);
    }

    private void handleFavouritesList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please log in to view favourites");
            return;
        }

        req.setAttribute("favourites", favouriteDAO.findByUserId(currentUser.getId()));
        req.setAttribute("activeNav", "favourites");
        req.setAttribute("activeSidebar", "favourites");
        req.setAttribute("pageTitle", "My Favourite Destinations - TripMate");

        req.getRequestDispatcher("/WEB-INF/views/destinations/favourites.jsp").forward(req, resp);
    }

    private void handleToggleFavourite(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=Please log in to save favourites");
            return;
        }

        String destId = req.getParameter("id");
        String redirect = req.getParameter("redirect");

        if (ValidationUtil.isNonEmpty(destId)) {
            if (favouriteDAO.isFavourited(currentUser.getId(), destId)) {
                favouriteDAO.removeFavourite(currentUser.getId(), destId);
            } else {
                favouriteDAO.addFavourite(currentUser.getId(), destId);
            }
        }

        if ("details".equalsIgnoreCase(redirect)) {
            resp.sendRedirect(req.getContextPath() + "/destination-details?id=" + destId);
        } else if ("favourites".equalsIgnoreCase(redirect)) {
            resp.sendRedirect(req.getContextPath() + "/favourites");
        } else {
            resp.sendRedirect(req.getContextPath() + "/destinations");
        }
    }
}
